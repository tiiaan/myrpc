package com.tiiaan.rpc.service.impl;

import com.tiiaan.rpc.annotation.MyService;
import com.tiiaan.rpc.service.HelloService;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@MyService(version = "0.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String message) {
        return "myrpc: " + message;
    }
}
