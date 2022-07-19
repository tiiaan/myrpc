package com.tiiaan.rpc.server.socket;

import com.tiiaan.rpc.common.config.MyRpcServerProperties;
import com.tiiaan.rpc.common.entity.MyRpcService;
import com.tiiaan.rpc.common.factory.SingletonFactory;
import com.tiiaan.rpc.server.AbstractRpcServer;
import com.tiiaan.rpc.common.enums.MyRpcError;
import com.tiiaan.rpc.common.exception.MyRpcException;
import com.tiiaan.rpc.common.factory.ThreadPoolFactory;
import com.tiiaan.rpc.handler.MyRpcRequestHandler;
import com.tiiaan.rpc.provider.ServiceProvider;
import com.tiiaan.rpc.provider.impl.ServiceProviderImpl;
import com.tiiaan.rpc.server.socket.thread.SocketRequestHandlerRunnable;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class SocketRpcServer extends AbstractRpcServer {

    private final ExecutorService threadPool;
    private final MyRpcRequestHandler myRpcRequestHandler;
    private final ServiceProvider serviceProvider;
    private final MyRpcServerProperties myRpcServerProperties;


    public SocketRpcServer() {
        this.myRpcRequestHandler = new MyRpcRequestHandler();
        threadPool = ThreadPoolFactory.createThreadPoolIfAbsent("socket-rpc-server");
        ThreadPoolFactory.monitorThreadPoolStatus((ThreadPoolExecutor) threadPool);
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
        myRpcServerProperties = SingletonFactory.getInstance(MyRpcServerProperties.class);
    }


    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket();) {
            String host = InetAddress.getLocalHost().getHostAddress();
            int port = myRpcServerProperties.getPort();
            serverSocket.bind(new InetSocketAddress(host, port));
            log.info("服务端 {}:{} 已启动, 等待客户端连接...", host, port);
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("客户端 {}:{} 连接成功", socket.getInetAddress().getHostAddress(), socket.getPort());
                threadPool.execute(new SocketRequestHandlerRunnable(socket, myRpcRequestHandler));
            }
        } catch (IOException e) {
            log.error("连接失败", e);
            throw new MyRpcException(MyRpcError.CONNECTION_FAILURE);
        } finally {
            threadPool.shutdown();
        }
    }


    //@Override
    //public void register(Object service) {
    //    serviceProvider.addService(service);
    //}

    @Override
    public void register(Object service, String version) {
        serviceProvider.publishService(new MyRpcService(service, version));
    }
}
