package com.tiiaan.rpc;

import com.tiiaan.rpc.annotation.EnableMyRpcScanner;
import com.tiiaan.rpc.controller.HelloController;
import com.tiiaan.rpc.entity.MyMessage;
import com.tiiaan.rpc.entity.MyRpcService;
import com.tiiaan.rpc.netty.NettyRpcClient;
import com.tiiaan.rpc.proxy.MyRpcClientProxy;
import com.tiiaan.rpc.service.HelloService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.TimeUnit;

@EnableMyRpcScanner(basePackage = {"com.tiiaan.rpc"})
public class ExampleClient {
    public static void main(String[] args) {

        //NettyRpcClient nettyRpcClient = new NettyRpcClient();
        //MyRpcClientProxy proxy = new MyRpcClientProxy(nettyRpcClient, new MyRpcService("1.0"));
        //HelloService helloService = proxy.getProxyInstance(HelloService.class);
        //MyMessage hello = helloService.hello(new MyMessage("hello world!"));
        //System.out.println(hello);

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExampleClient.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        for (int i = 0; i < 10; i++) {
            helloController.test();
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}