package com.tiiaan.rpc.service.impl;

import com.tiiaan.rpc.service.HelloTestService;
import com.tiiaan.rpc.spring.annotation.MyService;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@MyService
public class HelloTestServiceImpl implements HelloTestService {

    @Override
    public String hello(String message) {
        return message;
    }

}
