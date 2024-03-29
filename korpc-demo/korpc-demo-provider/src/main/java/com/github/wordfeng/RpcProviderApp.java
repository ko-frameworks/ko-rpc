package com.github.wordfeng;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.github")
@SpringBootApplication
public class RpcProviderApp {

    public static void main(String[] args) {
        SpringApplication.run(RpcProviderApp.class, args);
    }
}
