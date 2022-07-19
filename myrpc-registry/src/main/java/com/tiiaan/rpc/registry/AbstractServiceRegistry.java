package com.tiiaan.rpc.registry;

import cn.hutool.core.net.NetUtil;
import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import com.tiiaan.rpc.common.config.MyRpcServerProperties;
import com.tiiaan.rpc.common.factory.SingletonFactory;

import java.util.Set;



/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public abstract class AbstractServiceRegistry implements MyRpcServiceRegistry {

    private final String host;
    private final Integer port;
    private final MyRpcServerProperties myRpcServerProperties;

    private static final Set<String> registered = new ConcurrentHashSet<>();


    public AbstractServiceRegistry() {
        myRpcServerProperties = SingletonFactory.getInstance(MyRpcServerProperties.class);
        host = NetUtil.getLocalhostStr();
        port = myRpcServerProperties.getPort();
    }


    protected abstract void doRegister(String serviceKey, String host, int port);

    protected abstract void doUnregister(String serviceKey, String host, int port);



    @Override
    public void register(String serviceKey) {
        if (serviceKey == null || serviceKey.length() == 0) {
            throw new IllegalArgumentException("serviceKey should not be null or empty");
        }
        doRegister(serviceKey, host, port);
        registered.add(serviceKey);
    }


    @Override
    public void unregister(String serviceKey) {
        if (serviceKey == null || serviceKey.length() == 0) {
            throw new IllegalArgumentException("serviceKey should not be null or empty");
        }
        doUnregister(serviceKey, host, port);
        registered.remove(serviceKey);
    }


    @Override
    public void unregisterAll() {
        for (String serviceKey : registered) {
            unregister(serviceKey);
        }
    }

}
