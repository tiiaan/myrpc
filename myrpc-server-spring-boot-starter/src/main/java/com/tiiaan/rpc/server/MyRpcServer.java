package com.tiiaan.rpc.server;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public interface MyRpcServer {

    void start();

    void register(Object service, String version);

}
