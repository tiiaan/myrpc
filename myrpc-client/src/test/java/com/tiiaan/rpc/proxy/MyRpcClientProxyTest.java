package com.tiiaan.rpc.proxy;

import com.tiiaan.rpc.MyRpcClient;
import com.tiiaan.rpc.entity.MyMessage;
import com.tiiaan.rpc.netty.NettyRpcClient;
import com.tiiaan.rpc.service.HelloService;
import com.tiiaan.rpc.socket.SocketRpcClient;
import org.junit.Test;


public class MyRpcClientProxyTest {

    @Test
    public void getProxy() {
        //MyRpcClient socketRpcClient = new SocketRpcClient()
        //        .setServerHost("192.168.10.4")
        //        .setServerPort(9000);
        //MyRpcClientProxy proxy = new MyRpcClientProxy(socketRpcClient);


        MyRpcClient nettyRpcClient = new NettyRpcClient()
                .setServerHost("192.168.10.4")
                .setServerPort(9000);
        MyRpcClientProxy proxy = new MyRpcClientProxy(nettyRpcClient);

        HelloService helloService = proxy.getProxyInstance(HelloService.class);
        MyMessage returnObject = helloService.hello(new MyMessage("hello world!"));
        System.out.println(returnObject);
    }

    public static void main(String[] args) {

        //MyRpcClient socketRpcClient = new SocketRpcClient()
        //        .setServerHost("192.168.10.4")
        //        .setServerPort(9000);
        //MyRpcClientProxy proxy = new MyRpcClientProxy(socketRpcClient);

        MyRpcClient nettyRpcClient = new NettyRpcClient()
                .setServerHost("192.168.10.4")
                .setServerPort(9000);
        MyRpcClientProxy proxy = new MyRpcClientProxy(nettyRpcClient);

        HelloService helloService = proxy.getProxyInstance(HelloService.class);
        MyMessage returnObject = helloService.hello(new MyMessage("hello world!"));
        System.out.println(returnObject);
    }

}