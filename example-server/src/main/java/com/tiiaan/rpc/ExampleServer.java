package com.tiiaan.rpc;

import com.tiiaan.rpc.annotation.EnableMyServiceScan;
import com.tiiaan.rpc.server.netty.NettyRpcServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@EnableMyServiceScan(basePackage = {"com.tiiaan.rpc.service.impl"})
public class ExampleServer {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ExampleServer.class);
        NettyRpcServer nettyRpcServer = new NettyRpcServer();
        nettyRpcServer.start();
    }
}