package com.tiiaan.rpc.proxy;

import com.tiiaan.rpc.MyRpcClient;
import com.tiiaan.rpc.common.entity.MyRpcRequest;
import com.tiiaan.rpc.common.entity.MyRpcResponse;
import com.tiiaan.rpc.common.entity.MyRpcService;
import com.tiiaan.rpc.common.enums.MyRpcError;
import com.tiiaan.rpc.common.enums.ResponseStatus;
import com.tiiaan.rpc.common.exception.MyRpcException;
import com.tiiaan.rpc.netty.NettyRpcClient;
import com.tiiaan.rpc.socket.SocketRpcClient;
import com.tiiaan.rpc.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class MyRpcClientProxy implements InvocationHandler {

    private final MyRpcClient myRpcClient;
    private final MyRpcService myRpcService;

    public MyRpcClientProxy(MyRpcClient myRpcClient, MyRpcService myRpcService) {
        this.myRpcClient = myRpcClient;
        this.myRpcService = myRpcService;
    }

    public <T> T getProxyInstance(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //1.构造请求
        MyRpcRequest myRpcRequest = new MyRpcRequest().builder()
                .requestId(StringUtil.getUUID())
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .version(myRpcService.getVersion())
                .build();
        //2.向服务提供者发送请求
        MyRpcResponse<Object> myRpcResponse = null;
        if (myRpcClient instanceof NettyRpcClient) {
            try {
                CompletableFuture<MyRpcResponse<Object>> resultFuture
                        = (CompletableFuture<MyRpcResponse<Object>>) myRpcClient.sendRequest(myRpcRequest);
                myRpcResponse = resultFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("服务调用失败", e);
                return null;
            }
        }
        if (myRpcClient instanceof SocketRpcClient) {
            myRpcResponse = (MyRpcResponse<Object>) myRpcClient.sendRequest(myRpcRequest);
        }
        check(myRpcRequest, myRpcResponse);
        return myRpcResponse.getData();
    }


    public void check(MyRpcRequest myRpcRequest, MyRpcResponse<Object> myRpcResponse) {

        if (myRpcResponse == null ||
                myRpcResponse.getStatusCode() == null ||
                !myRpcResponse.getStatusCode().equals(ResponseStatus.SUCCESS.getCode())) {
            log.error("服务调用失败");
            throw new MyRpcException(MyRpcError.SERVICE_INVOCATION_FAILURE, "interface=" + myRpcRequest.getInterfaceName());
        }

        if (!myRpcRequest.getRequestId().equals(myRpcResponse.getRequestId())) {
            log.error("请求号不匹配");
            throw new MyRpcException(MyRpcError.RESPONSE_NOT_MATCH, "interface=" + myRpcRequest.getInterfaceName());
        }
    }

}
