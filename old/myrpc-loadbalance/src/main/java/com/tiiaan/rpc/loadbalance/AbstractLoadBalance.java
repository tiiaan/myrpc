package com.tiiaan.rpc.loadbalance;

import java.util.List;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public abstract class AbstractLoadBalance implements MyRpcLoadBalance {

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
