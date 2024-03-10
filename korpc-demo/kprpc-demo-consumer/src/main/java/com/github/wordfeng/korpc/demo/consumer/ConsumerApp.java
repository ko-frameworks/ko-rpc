package com.github.wordfeng.korpc.demo.consumer;

import com.github.wordfeng.korpc.core.consumer.ConsumerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

//@ComponentScan(basePackages = "com.github")
@Import(ConsumerConfiguration.class)
@SpringBootApplication
public class ConsumerApp {


    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }
}
