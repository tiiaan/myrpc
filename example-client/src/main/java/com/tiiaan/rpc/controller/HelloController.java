package com.tiiaan.rpc.controller;

import com.tiiaan.rpc.annotation.MyReference;
import com.tiiaan.rpc.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
@RestController
public class HelloController {


    @MyReference(version = "0.0")
    private HelloService helloService;


    @GetMapping("/hello")
    public String hello() {
        log.info("message");
        return helloService.hello("hello");
    }

}
