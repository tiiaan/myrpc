package com.tiiaan.rpc.config;


import com.tiiaan.rpc.bean.MyRpcClientProxyBeanPostProcessor;
import com.tiiaan.rpc.client.MyRpcClient;
import com.tiiaan.rpc.client.netty.ChannelProvider;
import com.tiiaan.rpc.client.netty.NettyRpcClient;
import com.tiiaan.rpc.client.netty.NettyRpcClientHandler;
import com.tiiaan.rpc.client.netty.UnprocessedRequests;
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
    @ConditionalOnMissingBean(MyRpcServiceDiscovery.class)
    public MyRpcServiceDiscovery myRpcServiceDiscovery() {
        log.info("...myRpcServiceDiscovery");
        return new NacosServiceDiscovery(myRpcClientProperties.getAddress());
    }


    @Bean
    @ConditionalOnMissingBean(UnprocessedRequests.class)
    public UnprocessedRequests unprocessedRequests() {
        log.info("...unprocessedRequests");
        return new UnprocessedRequests();
    }


    @Bean
    @ConditionalOnMissingBean(ChannelProvider.class)
    public ChannelProvider channelProvider() {
        log.info("...channelProvider");
        return new ChannelProvider();
    }


    @Bean
    @ConditionalOnBean(UnprocessedRequests.class)
    @ConditionalOnMissingBean(NettyRpcClientHandler.class)
    public NettyRpcClientHandler nettyRpcClientHandler() {
        log.info("...nettyRpcClientHandler");
        return new NettyRpcClientHandler();
    }


    @Bean
    @ConditionalOnBean({MyRpcServiceDiscovery.class, UnprocessedRequests.class, ChannelProvider.class})
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
