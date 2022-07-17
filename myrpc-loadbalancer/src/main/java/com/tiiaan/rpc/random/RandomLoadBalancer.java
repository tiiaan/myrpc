package com.tiiaan.rpc.random;

import com.tiiaan.rpc.AbstractLoadBalancer;

import java.util.List;
import java.util.Random;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * 随机负载均衡
 */

public class RandomLoadBalancer extends AbstractLoadBalancer {

    @Override
    protected <T> T doSelect(List<T> candidates) {
        Random random = new Random();
        return candidates.get(random.nextInt(candidates.size()));
    }

}
