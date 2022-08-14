package com.tiiaan.rpc.loadbalance;

import com.tiiaan.rpc.spi.MySPI;

import java.util.List;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@MySPI
public interface MyRpcLoadBalance {

    /**
     * 负载均衡器, 依据某种策略从若干服务提供者中选出一个
     * @param candidates 服务提供者地址列表
     * @return java.lang.String
     * @author tiiaan Email:tiiaan.w@gmail.com
     */
     <T> T select(List<T> candidates);

}
