package com.tiiaan.rpc.netty;

import com.tiiaan.rpc.provider.ServiceProvider;
import com.tiiaan.rpc.provider.impl.ServiceProviderImpl;
import com.tiiaan.rpc.service.HelloService;
import com.tiiaan.rpc.service.impl.HelloServiceImpl;
import org.junit.Test;


public class NettyRpcServerTest {

    @Test
    public void start() {

        HelloService helloService = new HelloServiceImpl();
        ServiceProvider serviceProvider = new ServiceProviderImpl();
        serviceProvider.addService(helloService);
        NettyRpcServer nettyRpcServer = new NettyRpcServer(9000);
        nettyRpcServer.start();

    }

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        //ServiceProvider serviceProvider = new ServiceProviderImpl();
        //serviceProvider.addService(helloService);
        NettyRpcServer nettyRpcServer = new NettyRpcServer(9004);
        nettyRpcServer.register(helloService);
        nettyRpcServer.start();
    }

}