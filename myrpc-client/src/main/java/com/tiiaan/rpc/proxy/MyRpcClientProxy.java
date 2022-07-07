package com.tiiaan.rpc.proxy;

import com.tiiaan.rpc.MyRpcClient;
import com.tiiaan.rpc.entity.MyRpcRequest;
import com.tiiaan.rpc.entity.MyRpcResponse;
import com.tiiaan.rpc.socket.SocketRpcClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class MyRpcClientProxy implements InvocationHandler {


    private final String host;
    private final Integer port;

    private MyRpcClient myRpcClient;

    public MyRpcClientProxy(String host, Integer port) {
        this.host = host;
        this.port = port;
        myRpcClient = new SocketRpcClient();
    }

    public <T> T getProxyInstance(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //1.构造请求
        MyRpcRequest myRpcRequest = new MyRpcRequest().builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        //2.向服务提供者发送请求
        log.info("向 {}:{} 发送 RPC 调用请求, interface={}, method={}, params={}",
                host, port, myRpcRequest.getInterfaceName(), myRpcRequest.getMethodName(), myRpcRequest.getParameters());
        return ((MyRpcResponse) myRpcClient.sendRequest(myRpcRequest, host, port)).getData();
    }

}
