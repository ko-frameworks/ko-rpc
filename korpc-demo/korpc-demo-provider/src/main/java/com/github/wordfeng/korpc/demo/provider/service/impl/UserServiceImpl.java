package com.github.wordfeng.korpc.demo.provider.service.impl;

import com.github.korpc.demo.api.UserService;
import com.github.korpc.demo.entity.User;
import com.github.wordfeng.korpc.core.annotation.RpcProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RpcProvider
@Component
public class UserServiceImpl implements UserService {
    @Override
    public User findById(Long id) {
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return new User(id, format);
    }
}
