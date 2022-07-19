package com.tiiaan.rpc.provider.impl;

import com.tiiaan.rpc.common.constants.Constants;
import com.tiiaan.rpc.spi.ExtensionLoader;
import com.tiiaan.rpc.registry.MyRpcServiceRegistry;
import com.tiiaan.rpc.common.entity.MyRpcService;
import com.tiiaan.rpc.common.enums.MyRpcError;
import com.tiiaan.rpc.common.exception.MyRpcException;
import com.tiiaan.rpc.provider.ServiceProvider;
import com.tiiaan.rpc.server.AbstractRpcServer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    private final Map<String, Object> interfaceMap;
    private final Set<String> registeredService;
    private final MyRpcServiceRegistry myRpcServiceRegistry;


    public ServiceProviderImpl() {
        interfaceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        myRpcServiceRegistry = ExtensionLoader.getExtensionLoader(MyRpcServiceRegistry.class).getExtension(Constants.DEFAULT_REGISTRY);
    }


    //public void addService(Object service) {
    //    String serviceName = service.getClass().getCanonicalName();
    //    if (registeredService.contains(serviceName)) {
    //        return;
    //    }
    //    registeredService.add(serviceName);
    //    Class<?>[] interfaces = service.getClass().getInterfaces();
    //    if (interfaces == null || interfaces.length == 0) {
    //        throw new MyRpcException(MyRpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
    //    }
    //    interfaceMap.put(interfaces[0].getCanonicalName(), service);
    //    log.info("服务注册成功 interface={}, serviceName={}, service={}", interfaces[0].getCanonicalName(), serviceName, service);
    //}


    public void addService(MyRpcService myRpcService) {
        String serviceKey = myRpcService.getServiceKey();
        if (registeredService.contains(serviceKey)) {
            return;
        }
        registeredService.add(serviceKey);
        interfaceMap.put(serviceKey, myRpcService.getService());
    }


    @Override
    public Object getService(String serviceKey) {
        Object service = interfaceMap.get(serviceKey);
        if (service == null) {
            throw new MyRpcException(MyRpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }



    @Override
    public void publishService(MyRpcService myRpcService) {
        this.addService(myRpcService);
        myRpcServiceRegistry.register(myRpcService.getServiceKey());
    }

}
