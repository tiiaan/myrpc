package com.tiiaan.rpc.proxy;

import com.tiiaan.rpc.entity.MyMessage;
import com.tiiaan.rpc.service.HelloService;
import org.junit.Test;


public class MyRpcClientProxyTest {

    @Test
    public void getProxy() {
        String host = "192.168.10.4";
        Integer port = 9000;
        MyRpcClientProxy proxy = new MyRpcClientProxy(host, port);
        HelloService helloService = proxy.getProxyInstance(HelloService.class);
        MyMessage returnObject = helloService.hello(new MyMessage("hello world!"));
        System.out.println(returnObject);
    }

}