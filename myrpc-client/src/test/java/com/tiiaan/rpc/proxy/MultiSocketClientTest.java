package com.tiiaan.rpc.proxy;

import com.tiiaan.rpc.MyRpcClient;
import com.tiiaan.rpc.entity.MyMessage;
import com.tiiaan.rpc.entity.MyRpcService;
import com.tiiaan.rpc.service.HelloService;
import com.tiiaan.rpc.socket.SocketRpcClient;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public class MultiSocketClientTest {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                100,
                100,
                1L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100),
                new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 100; i++) {
            threadPool.execute(() -> {
                MyRpcClient socketRpcClient = new SocketRpcClient()
                        .setServerHost("192.168.10.4")
                        .setServerPort(9000);
                MyRpcClientProxy proxy = new MyRpcClientProxy(socketRpcClient, new MyRpcService());
                try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
                HelloService helloService = proxy.getProxyInstance(HelloService.class);
                MyMessage returnObject = helloService.hello(new MyMessage("hello world!"));
                System.out.println("[" + Thread.currentThread().getName() + "] " + returnObject);
            });
        }
    }
}


