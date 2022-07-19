package com.tiiaan.rpc;

import com.tiiaan.rpc.spring.annotation.EnableMyRpcScanner;
import com.tiiaan.rpc.controller.HelloController;
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
        long start = 0L, end = 0L;
        for (int i = 0; i < 1000; i++) {
            helloController.test();
            try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
}