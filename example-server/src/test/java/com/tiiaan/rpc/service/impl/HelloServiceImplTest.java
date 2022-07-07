package com.tiiaan.rpc.service.impl;

import com.tiiaan.rpc.entity.MyMessage;
import org.junit.Test;

import static org.junit.Assert.*;

public class HelloServiceImplTest {

    @Test
    public void hello() {
        MyMessage myMessage = new MyMessage("hello world!");
        MyMessage returnObject = new HelloServiceImpl().hello(myMessage);
    }

}