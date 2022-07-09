package com.tiiaan.rpc;

import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public abstract class AbstractLoadBalancer implements MyRpcLoadBalancer {

    @Override
    public <T> T select(List<T> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        return doSelect(candidates);
    }

    protected abstract <T> T doSelect(List<T> candidates);
}
