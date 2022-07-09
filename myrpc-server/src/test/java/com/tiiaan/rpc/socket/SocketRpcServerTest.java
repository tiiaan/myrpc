package com.tiiaan.rpc.socket;

import com.tiiaan.rpc.factory.ThreadPoolFactory;
import com.tiiaan.rpc.provider.ServiceProvider;
import com.tiiaan.rpc.provider.impl.ServiceProviderImpl;
import com.tiiaan.rpc.service.HelloService;
import com.tiiaan.rpc.service.impl.HelloServiceImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class SocketRpcServerTest {

    @Test
    public void start() {
        HelloService helloService = new HelloServiceImpl();
        ServiceProvider serviceProvider = new ServiceProviderImpl();
        serviceProvider.addService(helloService);
        SocketRpcServer socketRpcServer = new SocketRpcServer(9000);
        socketRpcServer.start();
    }

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        //ServiceProvider serviceProvider = new ServiceProviderImpl();
        //serviceProvider.addService(helloService);
        SocketRpcServer socketRpcServer = new SocketRpcServer(9000);
        socketRpcServer.register(helloService);
        socketRpcServer.start();
    }

}