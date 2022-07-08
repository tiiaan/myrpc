package com.tiiaan.rpc.socket;

import com.tiiaan.rpc.AbstractRpcServer;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import com.tiiaan.rpc.factory.ThreadPoolFactory;
import com.tiiaan.rpc.handler.MyRpcRequestHandler;
import com.tiiaan.rpc.provider.ServiceProvider;
import com.tiiaan.rpc.provider.impl.ServiceProviderImpl;
import com.tiiaan.rpc.socket.thread.SocketRequestHandlerRunnable;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class SocketRpcServer extends AbstractRpcServer {

    private final ExecutorService threadPool;
    private final MyRpcRequestHandler myRpcRequestHandler;
    private final ServiceProvider serviceProvider = new ServiceProviderImpl();


    public SocketRpcServer(Integer port) {
        this.port = port;
        this.myRpcRequestHandler = new MyRpcRequestHandler();
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
    }


    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket();) {
            String host = InetAddress.getLocalHost().getHostAddress();
            serverSocket.bind(new InetSocketAddress(host, port));
            log.info("服务端 {}:{} 已启动, 等待客户端连接...", host, port);
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("客户端 {}:{} 连接成功", socket.getInetAddress().getHostAddress(), socket.getPort());
                threadPool.execute(new SocketRequestHandlerRunnable(socket, myRpcRequestHandler));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyRpcException(MyRpcError.CONNECTION_FAILURE);
        }
    }

    @Override
    public void register(Object service) {
        serviceProvider.addService(service);
    }

}
