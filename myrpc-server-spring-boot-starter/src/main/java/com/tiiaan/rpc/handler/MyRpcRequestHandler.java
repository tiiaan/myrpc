package com.tiiaan.rpc.handler;

import com.tiiaan.rpc.common.entity.MyRpcRequest;
import com.tiiaan.rpc.holder.ServiceHolder;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class MyRpcRequestHandler {

    @Resource
    private ServiceHolder serviceHolder;

    //public MyRpcRequestHandler() {
    //    serviceHolder = SingletonFactory.getInstance(ServiceHolder.class);
    //}


    public Object handle(MyRpcRequest myRpcRequest) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Object service = serviceHolder.getService(getServiceFullName(myRpcRequest));
        return invokeTargetMethod(myRpcRequest, service);
    }


    private String getServiceFullName(MyRpcRequest myRpcRequest) {
        return myRpcRequest.getInterfaceName() + myRpcRequest.getVersion();
    }


    private Object invokeTargetMethod(MyRpcRequest myRpcRequest, Object service) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object returnObject = null;
        Method method = service.getClass().getMethod(myRpcRequest.getMethodName(), myRpcRequest.getParamTypes());
        if (method != null) {
            returnObject = method.invoke(service, myRpcRequest.getParameters());
            log.info("方法调用成功 interface={}, method={}, params={}", myRpcRequest.getInterfaceName(), myRpcRequest.getMethodName(), myRpcRequest.getParameters());
        }
        return returnObject;
    }


    //private Object invokeTargetMethod(MyRpcRequest myRpcRequest, Object service) {
    //    Object returnObject = null;
    //    try {
    //        Method method = service.getClass().getMethod(myRpcRequest.getMethodName(), myRpcRequest.getParamTypes());
    //        if (method != null) {
    //            returnObject = method.invoke(service, myRpcRequest.getParameters());
    //            log.info("方法调用成功 interface={}, method={}, params={}", myRpcRequest.getInterfaceName(), myRpcRequest.getMethodName(), myRpcRequest.getParameters());
    //        }
    //    } catch (NoSuchMethodException e) {
    //        log.error("找不到方法", e);
    //        return MyRpcResponse.fail(ResponseStatus.METHOD_NOT_FOUND);
    //    } catch (IllegalAccessException | InvocationTargetException e) {
    //        log.error("方法调用失败", e);
    //        return MyRpcResponse.fail(ResponseStatus.FAIL);
    //    }
    //    return returnObject;
    //}

}
