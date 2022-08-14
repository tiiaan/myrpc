package com.tiiaan.rpc.registry.nacos;

import cn.hutool.core.net.NetUtil;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.tiiaan.rpc.common.enums.MyRpcError;
import com.tiiaan.rpc.common.exception.MyRpcException;
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
    private final String host;
    private final Integer port;



    public NacosServiceRegistry(Integer port, String address) {
        host = NetUtil.getLocalhostStr();
        this.port = port;
        try {
            namingService = NamingFactory.createNamingService(address);
            log.info("Nacos注册中心连接成功 [{}]", address);
        } catch (NacosException e) {
            log.error("Nacos注册中心连接失败", e);
            throw new MyRpcException(MyRpcError.SERVICE_REGISTRY_CONNECTION_FAILURE);
        }
    }


    @Override
    protected void doRegister(String serviceKey) {
        try {
            namingService.registerInstance(serviceKey, host, port);
            log.info("服务注册成功 [myrpc://{}:{}/{}]", host, port, serviceKey);
        } catch (NacosException e) {
            log.error("服务注册失败 [myrpc://{}:{}/{}]", host, port, serviceKey, e);
            throw new MyRpcException(MyRpcError.SERVICE_REGISTER_FAILURE);
        }
    }


    @Override
    protected void doUnregister(String serviceKey) {
        try {
            namingService.deregisterInstance(serviceKey, host, port);
            log.info("服务注销成功 [myrpc://{}:{}/{}]", host, port, serviceKey);
        } catch (NacosException e) {
            log.error("服务注销失败 [myrpc://{}:{}/{}]", host, port, serviceKey, e);
            throw new MyRpcException(MyRpcError.SERVICE_DEREGISTER_FAILURE);
        }
    }


}
