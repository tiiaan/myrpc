package com.tiiaan.rpc.server.socket;

import com.tiiaan.rpc.common.entity.MyRpcService;
import com.tiiaan.rpc.common.enums.MyRpcError;
import com.tiiaan.rpc.common.exception.MyRpcException;
import com.tiiaan.rpc.common.factory.ThreadPoolFactory;
import com.tiiaan.rpc.handler.MyRpcRequestHandler;
import com.tiiaan.rpc.holder.ServiceHolder;
import com.tiiaan.rpc.server.AbstractRpcServer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
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
    private Integer port;

    public void setPort(Integer port) {
        this.port = port;
    }

    @Resource
    private ServiceHolder serviceHolder;
    @Resource
    private MyRpcRequestHandler myRpcRequestHandler;


    public SocketRpcServer() {
        //this.myRpcRequestHandler = new MyRpcRequestHandler();
        threadPool = ThreadPoolFactory.createThreadPoolIfAbsent("socket-rpc-server");
        ThreadPoolFactory.monitorThreadPoolStatus((ThreadPoolExecutor) threadPool);
    }


    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket();) {
            String host = InetAddress.getLocalHost().getHostAddress();
            //int port = myRpcServerProperties.getPort();
            serverSocket.bind(new InetSocketAddress(host, port));
            log.info("服务端 {}:{} 已启动, 等待客户端连接...", host, port);
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("客户端 {}:{} 连接成功", socket.getInetAddress().getHostAddress(), socket.getPort());
                threadPool.execute(new SocketRpcServerHandlerRunnable(socket, myRpcRequestHandler));
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
        serviceHolder.setService(new MyRpcService(service, version));
    }
}
