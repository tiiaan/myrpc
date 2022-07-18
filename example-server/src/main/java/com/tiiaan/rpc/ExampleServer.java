package com.tiiaan.rpc;

import com.tiiaan.rpc.annotation.EnableMyRpcScanner;
import com.tiiaan.rpc.server.netty.NettyRpcServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@EnableMyRpcScanner(basePackage = {"com.tiiaan.rpc.service.impl"})
public class ExampleServer {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ExampleServer.class);
        NettyRpcServer nettyRpcServer = new NettyRpcServer();
        nettyRpcServer.start();
    }
}