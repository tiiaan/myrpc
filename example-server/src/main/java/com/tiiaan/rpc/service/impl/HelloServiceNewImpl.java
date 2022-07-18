package com.tiiaan.rpc.service.impl;

import com.tiiaan.rpc.annotation.MyService;
import com.tiiaan.rpc.entity.MyMessage;
import com.tiiaan.rpc.service.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@MyService(version = "1.0")
@Slf4j
public class HelloServiceNewImpl implements HelloService {
    @Override
    public MyMessage hello(MyMessage myMessage) {
        log.info("方法执行成功, {}", myMessage);
        return myMessage;
    }
}
