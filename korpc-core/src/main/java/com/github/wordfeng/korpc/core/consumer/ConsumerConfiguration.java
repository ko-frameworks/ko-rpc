package com.github.wordfeng.korpc.core.consumer;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class ConsumerConfiguration {

    @Bean
    @Order(1)
    ConsumerBootstrap createConsumer() {
        return new ConsumerBootstrap();
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    ApplicationRunner consumerInit(ConsumerBootstrap consumerBootstrap) {
        return c -> {
            consumerBootstrap.start();
            System.out.println("consumerStarted");
        };
    }

}
