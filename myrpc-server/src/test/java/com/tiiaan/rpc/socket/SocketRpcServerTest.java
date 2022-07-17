package com.tiiaan.rpc.socket;

import com.tiiaan.rpc.provider.ServiceProvider;
import com.tiiaan.rpc.provider.impl.ServiceProviderImpl;
import com.tiiaan.rpc.server.socket.SocketRpcServer;
import com.tiiaan.rpc.service.HelloService;
import com.tiiaan.rpc.service.impl.HelloServiceImpl;
import org.junit.Test;

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
        socketRpcServer.register(helloService, null);
        socketRpcServer.start();
    }

}