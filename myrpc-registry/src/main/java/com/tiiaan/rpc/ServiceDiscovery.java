package com.tiiaan.rpc;

import java.net.InetSocketAddress;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public interface ServiceDiscovery {

    InetSocketAddress lookupService(String serviceFullName);

}
