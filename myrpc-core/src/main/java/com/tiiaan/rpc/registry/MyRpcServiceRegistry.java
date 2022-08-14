package com.tiiaan.rpc.registry;


/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

//@MySPI
public interface MyRpcServiceRegistry {

    void register(String serviceKey);
    void unregister(String serviceKey);
    void unregisterAll();

}
