package com.tiiaan.rpc.controller;

import com.tiiaan.rpc.annotation.MyReference;
import com.tiiaan.rpc.entity.MyMessage;
import com.tiiaan.rpc.service.HelloService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Component
public class HelloController {

    @MyReference
    private HelloService helloService;

    public void test() {
        MyMessage hello = this.helloService.hello(new MyMessage("hello world!"));
        System.out.println(hello);
    }

}
