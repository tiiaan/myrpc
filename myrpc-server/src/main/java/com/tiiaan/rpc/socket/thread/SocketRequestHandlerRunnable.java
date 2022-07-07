package com.tiiaan.rpc.socket.thread;

import com.tiiaan.rpc.entity.MyRpcRequest;
import com.tiiaan.rpc.entity.MyRpcResponse;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import com.tiiaan.rpc.handler.MyRpcRequestHandler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class SocketRequestHandlerRunnable implements Runnable {

    private Socket socket;
    private MyRpcRequestHandler myRpcRequestHandler;

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());) {
            //读请求
            MyRpcRequest myRpcRequest = (MyRpcRequest) objectInputStream.readObject();
            ////从请求中获取要调用的的方法
            //Method method = service.getClass().getMethod(myRpcRequest.getMethodName(), myRpcRequest.getParamTypes());
            ////反射执行方法，获得返回值
            //Object returnObject = method.invoke(service, myRpcRequest.getParameters());

            Object returnObject = myRpcRequestHandler.handle(myRpcRequest);

            //返回值写进响应
            objectOutputStream.writeObject(MyRpcResponse.success(returnObject));
            objectOutputStream.flush();
            log.info("来自客户端 {}:{} 的调用请求处理成功", socket.getInetAddress().getHostAddress(), socket.getPort());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new MyRpcException(MyRpcError.IO_ERROR);
        }
    }

}
