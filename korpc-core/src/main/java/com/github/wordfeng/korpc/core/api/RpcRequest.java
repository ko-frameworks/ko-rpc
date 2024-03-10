package com.github.wordfeng.korpc.core.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest {

    private String service;
    private String method;
    private Class<?>[] argsType;
    private Object[] args;
}
