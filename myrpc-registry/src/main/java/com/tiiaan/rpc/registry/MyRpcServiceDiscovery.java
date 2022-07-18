package com.tiiaan.rpc.registry;

import com.tiiaan.rpc.MySPI;

import java.net.InetSocketAddress;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@MySPI
public interface MyRpcServiceDiscovery {

    InetSocketAddress lookupService(String serviceFullName);

}