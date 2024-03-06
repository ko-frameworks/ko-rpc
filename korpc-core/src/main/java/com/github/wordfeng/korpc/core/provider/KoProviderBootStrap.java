package com.github.wordfeng.korpc.core.provider;

import com.github.wordfeng.korpc.core.annotation.RpcProvider;
import com.github.wordfeng.korpc.core.api.RpcRequest;
import com.github.wordfeng.korpc.core.api.RpcResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/korpc")
public class KoProviderBootStrap implements ApplicationContextAware {

    private ApplicationContext context;

    private final Map<String, Object> skeleton = new HashMap<>();

    @RequestMapping("/endpoint")
    public RpcResponse invoke(@RequestBody RpcRequest rpcRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object bean = skeleton.get(rpcRequest.getService());
        Method method = bean.getClass().getMethod(rpcRequest.getMethod(), rpcRequest.getMethodArgs());
        Object result = method.invoke(bean, rpcRequest.getArgs());
        return new RpcResponse(true, result);
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
