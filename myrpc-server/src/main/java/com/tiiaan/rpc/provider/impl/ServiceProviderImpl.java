package com.tiiaan.rpc.provider.impl;

import com.tiiaan.rpc.ServiceRegistry;
import com.tiiaan.rpc.entity.MyRpcService;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import com.tiiaan.rpc.nacos.NacosServiceRegistry;
import com.tiiaan.rpc.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
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
    private static final ServiceRegistry serviceRegistry;


    static {
        interfaceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = new NacosServiceRegistry();
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
        interfaceMap.put(interfaces[0].getCanonicalName(), service);
        log.info("服务注册成功 interface={}, serviceName={}, service={}", interfaces[0].getCanonicalName(), serviceName, service);

        //for (Class<?> inter : interfaces) {
        //    interfaceMap.put(inter.getCanonicalName(), service);
        //    log.info("服务注册成功 interface={}, serviceName={}, service={}", inter.getCanonicalName(), serviceName, service);
        //}
    }

    @Override
    public void addService(MyRpcService myRpcService) {
        String serviceFullName = myRpcService.getServiceFullName();
        if (registeredService.contains(serviceFullName)) {
            return;
        }
        registeredService.add(serviceFullName);
        interfaceMap.put(serviceFullName, myRpcService.getService());
        //log.info("服务注册成功 interface={}, serviceName={}, service={}", myRpcService.getService().getClass().getInterfaces()[0].getCanonicalName(), serviceFullName, myRpcService.getService());
    }


    @Override
    public Object getService(String serviceFullName) {
        Object service = interfaceMap.get(serviceFullName);
        if (service == null) {
            throw new MyRpcException(MyRpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }


    @Override
    public void publishService(Object service, Integer port) {
        try {
            this.addService(service);
            String host = InetAddress.getLocalHost().getHostAddress();
            serviceRegistry.register(service.getClass().getInterfaces()[0].getCanonicalName(), new InetSocketAddress(host, port));
        } catch (UnknownHostException e) {
            log.error("获取localhost失败", e);
            throw new MyRpcException(MyRpcError.UNKNOWN_HOST);
        }
    }


    @Override
    public void publishService(MyRpcService myRpcService, Integer port) {
        try {
            this.addService(myRpcService);
            String host = InetAddress.getLocalHost().getHostAddress();
            serviceRegistry.register(myRpcService.getServiceFullName(), new InetSocketAddress(host, port));
        } catch (Exception e) {
            log.error("获取localhost失败", e);
            throw new MyRpcException(MyRpcError.UNKNOWN_HOST);
        }
    }

}
