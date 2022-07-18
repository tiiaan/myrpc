package com.tiiaan.rpc.provider;

import com.tiiaan.rpc.entity.MyRpcService;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public interface ServiceProvider {

    public void addService(Object service);
    public void addService(MyRpcService myRpcService);

    public Object getService(String serviceFullName);

    public void publishService(Object service);
    public void publishService(MyRpcService myRpcService);

}
