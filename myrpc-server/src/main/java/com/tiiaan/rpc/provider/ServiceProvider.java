package com.tiiaan.rpc.provider;

import com.tiiaan.rpc.common.entity.MyRpcService;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public interface ServiceProvider {

    //public void addService(MyRpcService myRpcService);
    public Object getService(String serviceKey);
    public void publishService(MyRpcService myRpcService);

}
