package com.tiiaan.rpc.holder;

import com.tiiaan.rpc.common.constants.Constants;
import com.tiiaan.rpc.common.entity.MyRpcService;
import com.tiiaan.rpc.common.enums.MyRpcError;
import com.tiiaan.rpc.common.exception.MyRpcException;
import com.tiiaan.rpc.common.spi.ExtensionLoader;
import com.tiiaan.rpc.registry.MyRpcServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class ServiceHolder {

    private final Map<String, Object> interfaceMap;
    private final Set<String> registeredService;
    private final MyRpcServiceRegistry myRpcServiceRegistry;


    public ServiceHolder() {
        interfaceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        myRpcServiceRegistry = ExtensionLoader.getExtensionLoader(MyRpcServiceRegistry.class).getExtension(Constants.DEFAULT_REGISTRY);
    }



    public void addService(MyRpcService myRpcService) {
        String serviceKey = myRpcService.getServiceKey();
        if (registeredService.contains(serviceKey)) {
            return;
        }
        registeredService.add(serviceKey);
        interfaceMap.put(serviceKey, myRpcService.getService());
    }


    public Object getService(String serviceKey) {
        Object service = interfaceMap.get(serviceKey);
        if (service == null) {
            throw new MyRpcException(MyRpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }


    public void setService(MyRpcService myRpcService) {
        this.addService(myRpcService);
        myRpcServiceRegistry.register(myRpcService.getServiceKey());
    }

}
