package com.tiiaan.rpc.config;


import com.tiiaan.rpc.bean.ApplicationContextUtils;
import com.tiiaan.rpc.bean.MyRpcClientProxyBeanPostProcessor;
import com.tiiaan.rpc.client.MyRpcClient;
import com.tiiaan.rpc.client.netty.ChannelProvider;
import com.tiiaan.rpc.client.netty.NettyRpcClient;
import com.tiiaan.rpc.client.netty.NettyRpcClientHandler;
import com.tiiaan.rpc.client.netty.FuturesHolder;
import com.tiiaan.rpc.registry.MyRpcServiceDiscovery;
import com.tiiaan.rpc.registry.nacos.NacosServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
@Configuration
@EnableConfigurationProperties(MyRpcClientProperties.class)
public class MyRpcClientAutoConfiguration {

    @Resource
    MyRpcClientProperties myRpcClientProperties;


    @Bean
    public ApplicationContextUtils applicationContextUtils() {
        return new ApplicationContextUtils();
    }


    @Bean
    @ConditionalOnMissingBean(MyRpcServiceDiscovery.class)
    public MyRpcServiceDiscovery myRpcServiceDiscovery() {
        log.info("...myRpcServiceDiscovery");
        return new NacosServiceDiscovery(myRpcClientProperties.getAddress());
    }


    @Bean
    @ConditionalOnMissingBean(FuturesHolder.class)
    public FuturesHolder unprocessedRequests() {
        log.info("...unprocessedRequests");
        return new FuturesHolder();
    }


    @Bean
    @ConditionalOnMissingBean(ChannelProvider.class)
    public ChannelProvider channelProvider() {
        log.info("...channelProvider");
        return new ChannelProvider();
    }


    @Bean
    @ConditionalOnBean(FuturesHolder.class)
    @ConditionalOnMissingBean(NettyRpcClientHandler.class)
    public NettyRpcClientHandler nettyRpcClientHandler() {
        log.info("...nettyRpcClientHandler");
        return new NettyRpcClientHandler();
    }


    @Bean
    @ConditionalOnBean({MyRpcServiceDiscovery.class, FuturesHolder.class, ChannelProvider.class})
    @ConditionalOnMissingBean(MyRpcClient.class)
    public MyRpcClient myRpcClient() {
        log.info("...myRpcClient");
        return new NettyRpcClient();
    }


    @Bean
    @ConditionalOnBean(MyRpcClient.class)
    @ConditionalOnMissingBean(MyRpcClientProxyBeanPostProcessor.class)
    public MyRpcClientProxyBeanPostProcessor myRpcClientProxyBeanPostProcessor() {
        log.info("...myRpcClientProxyBeanPostProcessor");
        return new MyRpcClientProxyBeanPostProcessor();
    }


}
