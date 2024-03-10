package com.github.korpc.demo.api;

import com.github.korpc.demo.entity.User;

public interface UserService {
    User findById(Long id);

    long echo(long id);
}
