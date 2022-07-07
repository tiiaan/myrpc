package com.tiiaan.rpc.socket;

import com.tiiaan.rpc.MyRpcServer;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import com.tiiaan.rpc.factory.ThreadPoolFactory;
import com.tiiaan.rpc.thread.SocketRequestHandlerRunnable;
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
public class SocketRpcServer implements MyRpcServer {

    private final ExecutorService threadPool;

    public SocketRpcServer() {
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
    }

    @Override
    public void start(Object service, Integer port) {
        try (ServerSocket serverSocket = new ServerSocket();) {
            String host = InetAddress.getLocalHost().getHostAddress();
            serverSocket.bind(new InetSocketAddress(host, port));
            log.info("服务端 {}:{} 已启动, 暴露服务 {}, 等待客户端连接...", host, port, service.getClass().getCanonicalName());
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("客户端 {}:{} 连接成功", socket.getInetAddress().getHostAddress(), socket.getPort());
                threadPool.execute(new SocketRequestHandlerRunnable(socket, service));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyRpcException(MyRpcError.CONNECTION_FAILURE);
        }
    }

}
