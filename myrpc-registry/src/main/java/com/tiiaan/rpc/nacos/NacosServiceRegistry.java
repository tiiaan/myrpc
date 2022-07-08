package com.tiiaan.rpc.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.tiiaan.rpc.ServiceRegistry;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerInstance(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            log.error("服务注册失败", e);
            throw new MyRpcException(MyRpcError.SERVICE_REGISTER_FAILURE);
        }
    }

}
