package com.tiiaan.rpc.handler;

import com.tiiaan.rpc.entity.MyRpcRequest;
import com.tiiaan.rpc.entity.MyRpcResponse;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.enums.ResponseStatus;
import com.tiiaan.rpc.exception.MyRpcException;
import com.tiiaan.rpc.provider.ServiceProvider;
import com.tiiaan.rpc.provider.impl.ServiceProviderImpl;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
@NoArgsConstructor
public class MyRpcRequestHandler {

    private ServiceProvider serviceProvider;


    public MyRpcRequestHandler(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }


    public Object handle(MyRpcRequest myRpcRequest) {
        Object service = serviceProvider.getService(myRpcRequest.getInterfaceName());
        return invokeTargetMethod(myRpcRequest, service);
    }


    private Object invokeTargetMethod(MyRpcRequest myRpcRequest, Object service) {
        Object returnObject = null;
        try {
            Method method = service.getClass().getMethod(myRpcRequest.getMethodName(), myRpcRequest.getParamTypes());
            if (method != null) {
                returnObject = method.invoke(service, myRpcRequest.getParameters());
                log.info("方法调用成功 interface={}, method={}, params={}", myRpcRequest.getInterfaceName(), myRpcRequest.getMethodName(), myRpcRequest.getParameters());
            }
        } catch (NoSuchMethodException e) {
            log.error("找不到方法", e);
            return MyRpcResponse.fail(ResponseStatus.METHOD_NOT_FOUND);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("方法调用失败", e);
            return MyRpcResponse.fail(ResponseStatus.FAIL);
        }
        return returnObject;
    }


}
