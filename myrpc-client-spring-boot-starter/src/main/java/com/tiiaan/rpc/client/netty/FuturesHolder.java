package com.tiiaan.rpc.client.netty;

import com.tiiaan.rpc.common.entity.MyRpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public class FuturesHolder {

    private static final Map<String, CompletableFuture<MyRpcResponse<Object>>> FUTURES = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<MyRpcResponse<Object>> future) {
        FUTURES.put(requestId, future);
    }

    public void remove(String requestId) {
        FUTURES.remove(requestId);
    }

    public void complete(MyRpcResponse<Object> myRpcResponse) {
        CompletableFuture<MyRpcResponse<Object>> completableFuture = FUTURES.remove(myRpcResponse.getRequestId());
        if (completableFuture != null) {
            completableFuture.complete(myRpcResponse);
        } else {
            throw new IllegalStateException();
        }
    }

}
