package com.github.wordfeng.korpc.core.provider;

import com.alibaba.fastjson2.JSON;
import com.github.wordfeng.korpc.core.annotation.RpcProvider;
import com.github.wordfeng.korpc.core.api.RpcRequest;
import com.github.wordfeng.korpc.core.api.RpcResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/korpc")
public class KoProviderBootstrap implements ApplicationContextAware {

    private ApplicationContext context;

    private final Map<String, Object> skeleton = new HashMap<>();

    @RequestMapping("/endpoint")
    public RpcResponse invoke(@RequestBody RpcRequest rpcRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object bean = skeleton.get(rpcRequest.getService());
        Method method = bean.getClass().getMethod(rpcRequest.getMethod(), rpcRequest.getArgsType());
        Class<?>[] argsType = rpcRequest.getArgsType();
        Object[] realArgs = new Object[argsType.length];
        for (int i = 0; i < argsType.length; i++) {
            Object realArg = JSON.to(argsType[i], rpcRequest.getArgs()[i]);
            realArgs[i] = realArg;
        }
        RpcResponse response = new RpcResponse();
        try {
            Object result = method.invoke(bean, realArgs);
            response.setData(result);
            response.setStatus(true);
            return response;
        }  catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
            response.setException(new RuntimeException(e.getTargetException().getMessage()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            response.setException(new RuntimeException(e.getMessage()));
        }
        response.setStatus(false);
        return response;
    }

    @PostConstruct
    public void buildProviders() {
        Map<String, Object> providers = context.getBeansWithAnnotation(RpcProvider.class);
        providers.forEach((x, y) -> System.out.println(x));
        providers.values().forEach(this::getInterface);
    }

    private void getInterface(Object x) {
        skeleton.put(x.getClass().getInterfaces()[0].getName(), x);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
