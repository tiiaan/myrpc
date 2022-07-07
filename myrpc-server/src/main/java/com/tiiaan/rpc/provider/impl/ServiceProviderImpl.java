package com.tiiaan.rpc.provider.impl;

import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import com.tiiaan.rpc.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    private static final Map<String, Object> interfaceMap;
    private static final Set<String> registeredService;

    static {
        interfaceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void addService(Object service) {
        String serviceName = service.getClass().getCanonicalName();
        if (registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces == null || interfaces.length == 0) {
            throw new MyRpcException(MyRpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for (Class<?> inter : interfaces) {
            interfaceMap.put(inter.getCanonicalName(), service);
            log.info("服务注册成功 interface={}, serviceName={}, service={}", inter.getCanonicalName(), serviceName, service);
        }
    }


    @Override
    public Object getService(String interfaceName) {
        Object service = interfaceMap.get(interfaceName);
        if (service == null) {
            throw new MyRpcException(MyRpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }

}
