package com.tiiaan.rpc;

import com.tiiaan.rpc.entity.MyRpcRequest;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public interface MyRpcClient {

    Object sendRequest(MyRpcRequest myRpcRequest, String host, Integer port);

}
