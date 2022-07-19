package com.tiiaan.rpc.loadbalance.round;

import com.tiiaan.rpc.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * 轮询负载均衡
 */

public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private final int getAndIncrement() {
        int current;
        int next;
        do {
            current = this.atomicInteger.get();
            next = current == Integer.MAX_VALUE ? 0 : current + 1;
        } while (!atomicInteger.compareAndSet(current, next));
        return next;
    }


    @Override
    protected <T> T doSelect(List<T> candidates) {
        return candidates.get(getAndIncrement() % candidates.size());
    }

}
