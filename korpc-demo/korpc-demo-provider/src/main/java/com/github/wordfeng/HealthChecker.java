package com.github.wordfeng;

import com.github.korpc.demo.api.UserService;
import com.github.wordfeng.korpc.core.api.RpcRequest;
import com.github.wordfeng.korpc.core.api.RpcResponse;
import com.github.wordfeng.korpc.core.provider.KoProviderBootstrap;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class HealthChecker implements ApplicationRunner, ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        KoProviderBootstrap bean = context.getBean(KoProviderBootstrap.class);
        RpcResponse response = bean.invoke(new RpcRequest(UserService.class.getName(), UserService.class.getMethod("findById", Long.class).getName(), new Class[]{Long.class}, new Object[]{1L}));
        System.out.println(response);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
