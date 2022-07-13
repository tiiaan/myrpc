package com.tiiaan.rpc.netty;

import com.tiiaan.rpc.entity.MyRpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public class UnprocessedRequests {

    private static Map<String, CompletableFuture<MyRpcResponse>> unprocessedFuture =
            new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<MyRpcResponse> future) {
        unprocessedFuture.put(requestId, future);
    }

    public void remove(String requestId) {
        unprocessedFuture.remove(requestId);
    }

    public void complete(MyRpcResponse myRpcResponse) {
        CompletableFuture<MyRpcResponse> completableFuture = unprocessedFuture.get(myRpcResponse.getRequestId());
        if (completableFuture != null) {
            completableFuture.complete(myRpcResponse);
        } else {
            throw new IllegalStateException();
        }
    }

}
