package com.tiiaan.rpc;

import com.tiiaan.rpc.entity.MyMessage;
import com.tiiaan.rpc.entity.MyRpcService;
import com.tiiaan.rpc.netty.NettyRpcClient;
import com.tiiaan.rpc.proxy.MyRpcClientProxy;
import com.tiiaan.rpc.service.HelloService;

public class ExampleClient {
    public static void main(String[] args) {
        NettyRpcClient nettyRpcClient = new NettyRpcClient();
        MyRpcClientProxy proxy = new MyRpcClientProxy(nettyRpcClient, new MyRpcService(""));
        HelloService helloService = proxy.getProxyInstance(HelloService.class);
        MyMessage hello = helloService.hello(new MyMessage("hello world!"));
        System.out.println(hello);
    }
}