package com.tiiaan.rpc.registry;

import com.tiiaan.rpc.common.spi.MySPI;

import java.net.InetSocketAddress;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

//@MySPI
public interface MyRpcServiceDiscovery {

    InetSocketAddress lookup(String serviceKey);

    void subscribe(String serviceKey);

}
