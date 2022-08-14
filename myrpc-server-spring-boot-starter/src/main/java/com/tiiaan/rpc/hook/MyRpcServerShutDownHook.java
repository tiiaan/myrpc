package com.tiiaan.rpc.hook;

import com.tiiaan.rpc.common.constants.Constants;
import com.tiiaan.rpc.common.factory.ThreadPoolFactory;
import com.tiiaan.rpc.common.spi.ExtensionLoader;
import com.tiiaan.rpc.registry.MyRpcServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class MyRpcServerShutDownHook {

    @Resource
    private MyRpcServiceRegistry myRpcServiceRegistry;

    private static final MyRpcServerShutDownHook instance = new MyRpcServerShutDownHook();


    private MyRpcServerShutDownHook() {
        //myRpcServiceRegistry = ExtensionLoader.getExtensionLoader(MyRpcServiceRegistry.class).getExtension(Constants.DEFAULT_REGISTRY);
    }

    public static MyRpcServerShutDownHook getShutDownHook() {
        return instance;
    }

    public void start() {
        log.info("hook start");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("shutdown hook");
            myRpcServiceRegistry.unregisterAll();
            ThreadPoolFactory.shutDownAllThreadPools();
        }));
    }

}
