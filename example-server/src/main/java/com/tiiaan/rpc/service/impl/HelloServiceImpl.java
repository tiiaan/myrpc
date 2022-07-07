package com.tiiaan.rpc.service.impl;

import com.tiiaan.rpc.entity.MyMessage;
import com.tiiaan.rpc.service.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    public MyMessage hello(MyMessage myMessage) {
        log.info("HelloServiceImpl.hello(): get myMessage, id={}, message={}", myMessage.getId(), myMessage.getMessage());
        return myMessage;
    }

}
