package com.tiiaan.rpc.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.tiiaan.rpc.MyRpcLoadBalancer;
import com.tiiaan.rpc.ServiceDiscovery;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import com.tiiaan.rpc.factory.SingletonFactory;
import com.tiiaan.rpc.random.RandomLoadBalancer;
import com.tiiaan.rpc.round.RoundRobinLoadBalancer;
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

    private final MyRpcLoadBalancer myRpcLoadBalancer;


    public NacosServiceDiscovery() {
        //myRpcLoadBalancer = new RandomLoadBalancer();
        myRpcLoadBalancer = SingletonFactory.getInstance(RoundRobinLoadBalancer.class);
        //myRpcLoadBalancer = SingletonFactory.getInstance(RandomLoadBalancer.class);
    }

    @Override
    public InetSocketAddress lookupService(String serviceFullName) {
        log.info("lookup service {}", serviceFullName);
        Instance instance = getInstance(serviceFullName);
        return new InetSocketAddress(instance.getIp(), instance.getPort());
    }


    private Instance getInstance(String serviceName) {
        try {
            List<Instance> instances = NacosUtil.getAllInstances(serviceName);
            if (instances == null || instances.size() == 0) {
                log.error("找不到对应的服务 {}", instances);
                throw new MyRpcException(MyRpcError.SERVICE_NOT_FOUND);
            }
            return myRpcLoadBalancer.select(instances);
        } catch (NacosException e) {
            log.error("服务发现失败", e);
            throw new MyRpcException(MyRpcError.SERVICE_DISCOVERY_FAILURE);
        }
    }

}
