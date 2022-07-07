package com.tiiaan.rpc.provider;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public interface ServiceProvider {

    public void addService(Object service);
    public Object getService(String interfaceName);

}
