package com.tiiaan.rpc.hook;

import com.tiiaan.rpc.factory.ThreadPoolFactory;
import com.tiiaan.rpc.registry.nacos.NacosUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class MyRpcServerShutDownHook {

    private static final MyRpcServerShutDownHook instance = new MyRpcServerShutDownHook();

    private MyRpcServerShutDownHook() {
    }

    public static MyRpcServerShutDownHook getShutDownHook() {
        return instance;
    }

    public void start() {
        log.info("hook start");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("shutdown hook");
            NacosUtil.deregisterAllInstances();
            ThreadPoolFactory.shutDownAllThreadPools();
        }));
    }

}
