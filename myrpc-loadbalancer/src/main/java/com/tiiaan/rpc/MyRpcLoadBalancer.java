package com.tiiaan.rpc;

import java.util.List;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public interface MyRpcLoadBalancer {

    /**
     * 负载均衡器, 依据某种策略, 从若干服务提供者中选出一个
     * @param candidates 服务提供者地址列表
     * @return java.lang.String
     * @author tiiaan Email:tiiaan.w@gmail.com
     */
     <T> T select(List<T> candidates);

}