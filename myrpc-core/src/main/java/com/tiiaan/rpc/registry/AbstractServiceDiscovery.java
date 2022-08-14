package com.tiiaan.rpc.registry;

import com.tiiaan.rpc.common.constants.Constants;
import com.tiiaan.rpc.common.spi.ExtensionLoader;
import com.tiiaan.rpc.common.util.StringUtil;
import com.tiiaan.rpc.loadbalance.MyRpcLoadBalance;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public abstract class AbstractServiceDiscovery implements MyRpcServiceDiscovery {

    private static final Map<String, Set<String>> notified = new ConcurrentHashMap<>();
    private final MyRpcLoadBalance myRpcLoadBalance =
            ExtensionLoader.getExtensionLoader(MyRpcLoadBalance.class).getExtension(Constants.DEFAULT_LOAD_BALANCE);


    protected abstract List<String> doLookup(String serviceKey);

    protected abstract void doSubscribe(String serviceKey);


    @Override
    public InetSocketAddress lookup(String serviceKey) {
        List<String> candidates = lookupNotified(serviceKey);
        String selected = myRpcLoadBalance.select(candidates);
        log.info("命中节点 [{}]", selected);
        return StringUtil.buildAddr(selected);
    }


    @Override
    public void subscribe(String serviceKey) {
        doSubscribe(serviceKey);
    }


    private List<String> lookupNotified(String serviceKey) {
        if (notified.containsKey(serviceKey)) {
            return new ArrayList<>(notified.get(serviceKey));
        }
        //如果缓存中没有, 调用doLookup()方法向注册中心请求服务提供者地址列表, 并保存到缓存中
        List<String> candidates = doLookup(serviceKey);
        for (String serviceAddress : candidates) {
            addNotified(serviceKey, serviceAddress);
        }
        doSubscribe(serviceKey);
        return candidates;
    }


    public void resetNotified(String serviceKey, Set<String> candidates) {
        notified.remove(serviceKey);
        notified.putIfAbsent(serviceKey, candidates);
        log.info("更新节点缓存 [{}]", serviceKey);
    }


    private void addNotified(String serviceKey, String serviceAddress) {
        Set<String> candidates = notified.get(serviceKey);
        if (!notified.containsKey(serviceKey)) {
            notified.putIfAbsent(serviceKey, new HashSet<>());
            candidates = notified.get(serviceKey);
        }
        candidates.add(serviceAddress);
    }


    private void removeNotified(String serviceKey, String serviceAddress) {
        if (notified.containsKey(serviceKey)) {
            notified.get(serviceKey).remove(serviceAddress);
        }
    }

}
