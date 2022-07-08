package com.tiiaan.rpc.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class NacosUtil {

    private static final NamingService namingService;
    private static final String NACOS_SERVER_ADDRESS = "172.16.7.129:1111";

    static {
        try {
            namingService = NamingFactory.createNamingService(NACOS_SERVER_ADDRESS);
            log.info("Nacos注册中心 {} 连接成功, status={}", NACOS_SERVER_ADDRESS, namingService.getServerStatus());
        } catch (NacosException e) {
            log.error("Nacos注册中心连接失败", e);
            throw new MyRpcException(MyRpcError.SERVICE_REGISTRY_CONNECTION_FAILURE);
        }
    }

    public static void registerInstance(String serviceName, InetSocketAddress inetSocketAddress) throws NacosException {
        String host = inetSocketAddress.getAddress().getHostAddress();
        int port = inetSocketAddress.getPort();
        namingService.registerInstance(serviceName, host, port);
        log.info("服务注册成功, service={}, address={}:{}", serviceName, host, port);
    }

    public static List<Instance> getAllInstances(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }
}
