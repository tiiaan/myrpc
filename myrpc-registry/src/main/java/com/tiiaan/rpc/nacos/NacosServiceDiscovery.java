package com.tiiaan.rpc.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.tiiaan.rpc.ServiceDiscovery;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {


    @Override
    public InetSocketAddress lookupService(String serviceName) {
        Instance instance = getInstance(serviceName);
        return new InetSocketAddress(instance.getIp(), instance.getPort());
    }


    private Instance getInstance(String serviceName) {
        try {
            List<Instance> instances = NacosUtil.getAllInstances(serviceName);
            if (instances == null || instances.size() == 0) {
                log.error("找不到对应的服务");
                throw new MyRpcException(MyRpcError.SERVICE_NOT_FOUND);
            }
            return instances.get(0);
        } catch (NacosException e) {
            log.error("服务发现失败", e);
            throw new MyRpcException(MyRpcError.SERVICE_DISCOVERY_FAILURE);
        }
    }

}
