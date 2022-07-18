package com.tiiaan.rpc.proxy;

import com.tiiaan.rpc.MyRpcClient;
import com.tiiaan.rpc.entity.MyRpcService;
import com.tiiaan.rpc.netty.NettyRpcClient;
import com.tiiaan.rpc.socket.SocketRpcClient;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public class SocketClientTest {

    //public static void main(String[] args) {
    //
    //    MyRpcClient socketRpcClient = new SocketRpcClient()
    //            .setServerHost("192.168.10.4")
    //            .setServerPort(9000);
    //    MyRpcClientProxy proxy = new MyRpcClientProxy(socketRpcClient, new MyRpcService());
    //    HelloService helloService = proxy.getProxyInstance(HelloService.class);
    //    MyMessage returnObject = helloService.hello(new MyMessage("hello world!"));
    //    System.out.println(returnObject);
    //}

}
