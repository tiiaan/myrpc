package com.tiiaan.rpc.socket;

import com.tiiaan.rpc.service.HelloService;
import com.tiiaan.rpc.service.impl.HelloServiceImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class SocketRpcServerTest {

    @Test
    public void start() {

        HelloService helloService = new HelloServiceImpl();
        SocketRpcServer socketRpcServer = new SocketRpcServer();
        socketRpcServer.start(helloService, 9000);
        
    }
}