package com.tiiaan.rpc.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.tiiaan.rpc.common.config.MyRpcClientProperties;
import com.tiiaan.rpc.common.config.MyRpcServerProperties;
import com.tiiaan.rpc.common.enums.MyRpcError;
import com.tiiaan.rpc.common.exception.MyRpcException;
import com.tiiaan.rpc.common.factory.SingletonFactory;
import com.tiiaan.rpc.registry.AbstractServiceRegistry;
import lombok.extern.slf4j.Slf4j;



/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class NacosServiceRegistry extends AbstractServiceRegistry {

    private final NamingService namingService;
    private final MyRpcServerProperties myRpcServerProperties;


    public NacosServiceRegistry() {
        myRpcServerProperties = SingletonFactory.getInstance(MyRpcServerProperties.class);
        try {
            namingService = NamingFactory.createNamingService(myRpcServerProperties.getRegistryAddr());
            log.info("Nacos注册中心连接成功 [{}]", myRpcServerProperties.getRegistryAddr());
        } catch (NacosException e) {
            log.error("Nacos注册中心连接失败", e);
            throw new MyRpcException(MyRpcError.SERVICE_REGISTRY_CONNECTION_FAILURE);
        }
    }


    @Override
    protected void doRegister(String serviceKey, String host, int port) {
        try {
            namingService.registerInstance(serviceKey, host, port);
            log.info("服务注册成功 [myrpc://{}:{}/{}]", host, port, serviceKey);
        } catch (NacosException e) {
            log.error("服务注册失败 [myrpc://{}:{}/{}]", host, port, serviceKey, e);
            throw new MyRpcException(MyRpcError.SERVICE_REGISTER_FAILURE);
        }
    }


    @Override
    protected void doUnregister(String serviceKey, String host, int port) {
        try {
            namingService.deregisterInstance(serviceKey, host, port);
            log.info("服务注销成功 [myrpc://{}:{}/{}]", host, port, serviceKey);
        } catch (NacosException e) {
            log.error("服务注销失败 [myrpc://{}:{}/{}]", host, port, serviceKey, e);
            throw new MyRpcException(MyRpcError.SERVICE_DEREGISTER_FAILURE);
        }
    }


}
