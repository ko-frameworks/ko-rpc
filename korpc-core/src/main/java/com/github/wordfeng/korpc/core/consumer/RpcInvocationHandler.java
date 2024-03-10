package com.github.wordfeng.korpc.core.consumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.wordfeng.korpc.core.api.RpcRequest;
import com.github.wordfeng.korpc.core.api.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RpcInvocationHandler implements InvocationHandler {

    public RpcInvocationHandler(Class<?> service) {
        this.service = service;
    }

    private final Class<?> service;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isLocalMethod(method.getName())) {
            return null;
        }
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setService(service.getCanonicalName());
        rpcRequest.setMethod(method.getName());
        rpcRequest.setArgs(args);
        rpcRequest.setArgsType(method.getParameterTypes());

        RpcResponse response = invoke0(rpcRequest);
        if (response == null) {
            return new RuntimeException(String.format("Invoke class [%s] method [%s(%s)] error, params:[%S]", service, method.getName(), Arrays.toString(method.getParameterTypes()), Arrays.toString(args)));
        }
        if (response.isStatus()) {
            if (response.getData() instanceof JSONObject jsonResult) {
                return jsonResult.toJavaObject(method.getReturnType());
            } else {
                return JSON.to(method.getReturnType(), response.getData());
            }
        }
        Exception exception = response.getException();
        throw new RuntimeException(exception);
    }

    OkHttpClient client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(16, 60, TimeUnit.SECONDS))
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .build();
    final static MediaType mediaTYpe = MediaType.get("application/json; charset=utf-8");

    private RpcResponse invoke0(RpcRequest rpcRequest) throws IOException {
        String requestBody = JSON.toJSONString(rpcRequest);
        Request post = new Request
                .Builder()
                .url("http://localhost:8080/korpc/endpoint")
                .post(RequestBody.create(requestBody.getBytes(), mediaTYpe)
                ).build();
        Response response = client.newCall(post).execute();
        if (response.body() == null) {
            return null;
        }
        try {
            String result = response.body().string();
            return JSON.parseObject(result, RpcResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private boolean isLocalMethod(String method) {
        if ("toString".equals(method) ||
                "clone".equals(method) ||
                "hashCode".equals(method) ||
                "equals".equals(method) ||
                "wait".equals(method) ||
                "getClass".equals(method) ||
                "notifyAll".equals(method) ||
                "notify".equals(method)) {
            return true;
        }
        return false;
    }
}
