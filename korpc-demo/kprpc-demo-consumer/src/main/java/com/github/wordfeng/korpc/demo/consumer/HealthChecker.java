package com.github.wordfeng.korpc.demo.consumer;

import com.github.korpc.demo.api.UserService;
import com.github.korpc.demo.entity.User;
import com.github.wordfeng.korpc.core.annotation.RpcConsumer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class HealthChecker implements ApplicationRunner {

    @RpcConsumer
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = userService.findById(123L);
        System.out.println(user);
        System.out.println(userService.echo(45888888888886L));
        try {
            User exception = userService.findById(4L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
