package com.github.wordfeng.korpc.core.api;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RpcResponse {
    private boolean status;
    Object data;
}
