package com.tiiaan.rpc.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.tiiaan.rpc.common.config.MyRpcClientProperties;
import com.tiiaan.rpc.common.config.MyRpcServerProperties;
import com.tiiaan.rpc.common.enums.MyRpcError;
import com.tiiaan.rpc.common.exception.MyRpcException;
import com.tiiaan.rpc.common.factory.SingletonFactory;
import com.tiiaan.rpc.registry.AbstractServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class NacosServiceDiscovery extends AbstractServiceDiscovery {


    private final NamingService namingService;
    private final MyRpcClientProperties myRpcClientProperties;


    public NacosServiceDiscovery() {
        myRpcClientProperties = SingletonFactory.getInstance(MyRpcClientProperties.class);
        try {
            namingService = NamingFactory.createNamingService(myRpcClientProperties.getRegistryAddr());
            log.info("Nacos注册中心连接成功 [{}]", myRpcClientProperties.getRegistryAddr());
        } catch (NacosException e) {
            log.error("Nacos注册中心连接失败", e);
            throw new MyRpcException(MyRpcError.SERVICE_REGISTRY_CONNECTION_FAILURE);
        }
    }


    @Override
    protected List<String> doLookup(String serviceKey) {
        List<Instance> instances;
        try {
            instances = namingService.getAllInstances(serviceKey);
        } catch (NacosException e) {
            log.error("服务发现失败 [{}]", serviceKey, e);
            throw new MyRpcException(MyRpcError.SERVICE_DISCOVERY_FAILURE);
        }
        List<String> candidates = new ArrayList<>();
        String address = null;
        if (instances != null) {
            for (Instance instance : instances) {
                address = instance == null ? null : instance.getIp() + ":" + instance.getPort();
                candidates.add(address);
            }
        }
        return candidates;
    }


    @Override
    protected void doSubscribe(String serviceKey) {
        try {
            namingService.subscribe(serviceKey, event -> {
                if (event instanceof NamingEvent) {
                    log.info("监听到节点变化 [{}]", serviceKey);
                    NamingEvent namingEvent = (NamingEvent) event;
                    List<Instance> instances = namingEvent.getInstances();
                    super.resetNotified(serviceKey, getCandidateSet(instances));
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }


    private Set<String> getCandidateSet(List<Instance> instances) {
        Set<String> candidates = new HashSet<>();
        String address = null;
        if (instances != null) {
            for (Instance instance : instances) {
                address = instance == null ? null : instance.getIp() + ":" + instance.getPort();
                candidates.add(address);
            }
        }
        return candidates;
    }

}
