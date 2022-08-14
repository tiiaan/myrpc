package com.tiiaan.rpc.config;

import com.tiiaan.rpc.bean.MyRpcServerRegisterBeanPostProcessor;
import com.tiiaan.rpc.handler.MyRpcRequestHandler;
import com.tiiaan.rpc.holder.ServiceHolder;
import com.tiiaan.rpc.registry.MyRpcServiceRegistry;
import com.tiiaan.rpc.registry.nacos.NacosServiceRegistry;
import com.tiiaan.rpc.server.MyRpcServer;
import com.tiiaan.rpc.server.netty.NettyRpcServer;
import com.tiiaan.rpc.server.netty.NettyRpcServerHandler;
import com.tiiaan.rpc.server.socket.SocketRpcServer;
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
@EnableConfigurationProperties(MyRpcServerProperties.class)
public class MyRpcServerAutoConfiguration {

    @Resource
    MyRpcServerProperties myRpcServerProperties;


    @Bean
    @ConditionalOnMissingBean(ServiceHolder.class)
    public ServiceHolder serviceHolder() {
        log.info("...serviceHolder");
        return new ServiceHolder();
    }


    @Bean
    @ConditionalOnBean(ServiceHolder.class)
    @ConditionalOnMissingBean(MyRpcServerRegisterBeanPostProcessor.class)
    public MyRpcServerRegisterBeanPostProcessor registerBeanPostProcessor() {
        log.info("...registerBeanPostProcessor");
        return new MyRpcServerRegisterBeanPostProcessor();
    }


    @Bean
    @ConditionalOnBean(ServiceHolder.class)
    @ConditionalOnMissingBean(MyRpcServer.class)
    public MyRpcServer nettyRpcServer() {
        log.info("...nettyRpcServer");
        NettyRpcServer nettyRpcServer = new NettyRpcServer();
        nettyRpcServer.setPort(myRpcServerProperties.getPort());
        return nettyRpcServer;
    }


    @Bean
    @ConditionalOnBean(ServiceHolder.class)
    @ConditionalOnMissingBean(MyRpcServer.class)
    public MyRpcServer socketRpcServer() {
        log.info("...socketRpcServer");
        SocketRpcServer socketRpcServer = new SocketRpcServer();
        socketRpcServer.setPort(myRpcServerProperties.getPort());
        return socketRpcServer;
    }


    @Bean
    @ConditionalOnBean(ServiceHolder.class)
    @ConditionalOnMissingBean(MyRpcRequestHandler.class)
    public MyRpcRequestHandler myRpcRequestHandler() {
        log.info("...myRpcRequestHandler");
        return new MyRpcRequestHandler();
    }


    @Bean
    @ConditionalOnBean(MyRpcRequestHandler.class)
    @ConditionalOnMissingBean(NettyRpcServerHandler.class)
    public NettyRpcServerHandler nettyRpcServerHandler() {
        log.info("...nettyRpcServerHandler");
        return new NettyRpcServerHandler();
    }


    @Bean
    @ConditionalOnMissingBean(MyRpcServiceRegistry.class)
    public MyRpcServiceRegistry myRpcServiceRegistry() {
        log.info("...myRpcServiceRegistry");
        return new NacosServiceRegistry(myRpcServerProperties.getPort(), myRpcServerProperties.getAddress());
    }

}
