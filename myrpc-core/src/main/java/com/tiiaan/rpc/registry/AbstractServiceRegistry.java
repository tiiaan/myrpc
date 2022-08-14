package com.tiiaan.rpc.registry;

import com.alibaba.nacos.common.utils.ConcurrentHashSet;

import java.util.Set;



/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public abstract class AbstractServiceRegistry implements MyRpcServiceRegistry {

    //private final MyRpcServerProperties myRpcServerProperties;

    private static final Set<String> registered = new ConcurrentHashSet<>();


    //public AbstractServiceRegistry() {
        //myRpcServerProperties = SingletonFactory.getInstance(MyRpcServerProperties.class);
        //port = myRpcServerProperties.getPort();
    //}


    protected abstract void doRegister(String serviceKey);

    protected abstract void doUnregister(String serviceKey);



    @Override
    public void register(String serviceKey) {
        if (serviceKey == null || serviceKey.length() == 0) {
            throw new IllegalArgumentException("serviceKey should not be null or empty");
        }
        doRegister(serviceKey);
        registered.add(serviceKey);
    }


    @Override
    public void unregister(String serviceKey) {
        if (serviceKey == null || serviceKey.length() == 0) {
            throw new IllegalArgumentException("serviceKey should not be null or empty");
        }
        doUnregister(serviceKey);
        registered.remove(serviceKey);
    }


    @Override
    public void unregisterAll() {
        for (String serviceKey : registered) {
            unregister(serviceKey);
        }
    }

}
