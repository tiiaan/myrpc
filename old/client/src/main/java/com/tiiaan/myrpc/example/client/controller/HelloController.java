package com.tiiaan.myrpc.example.client.controller;

import com.tiiaan.rpc.entity.MyMessage;
import com.tiiaan.rpc.service.HelloService;
import com.tiiaan.rpc.service.HelloTestService;
import com.tiiaan.rpc.spring.annotation.MyReference;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@RestController
public class HelloController {

    @MyReference
    private HelloTestService helloTestService;


    @GetMapping("/hello")
    public String hello() {
        return this.helloTestService.hello("hello world!");
    }

}
