package com.tiiaan.rpc.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class NacosUtil {

    private static final NamingService namingService;
    private static final String NACOS_SERVER_ADDRESS = "172.16.7.129:1111";
    private static final boolean USE_IP_MODE = true;

    private static InetSocketAddress registeredAddress;
    private static final Set<String> registeredServices;

    static {
        try {
            namingService = NamingFactory.createNamingService(NACOS_SERVER_ADDRESS);
            registeredServices = new HashSet<>();
            log.info("Nacos注册中心 {} 连接成功, status={}", NACOS_SERVER_ADDRESS, namingService.getServerStatus());
        } catch (NacosException e) {
            log.error("Nacos注册中心连接失败", e);
            throw new MyRpcException(MyRpcError.SERVICE_REGISTRY_CONNECTION_FAILURE);
        }
    }

    /**
     * 向Nacos注册服务
     * @param serviceName 服务注册名
     * @param inetSocketAddress 服务提供者地址
     * @author tiiaan Email:tiiaan.w@gmail.com
     */
    public static void registerInstance(String serviceName, InetSocketAddress inetSocketAddress) throws NacosException {
        NacosUtil.registeredAddress = inetSocketAddress;
        String host = getRegisterHost(inetSocketAddress);
        int port = getRegisterPort(inetSocketAddress);
        namingService.registerInstance(serviceName, host, port);
        registeredServices.add(serviceName);
        log.info("服务 {}@{}:{} 注册成功", serviceName, host, port);
    }


    /**
     * 从Nacos注册中心获取服务
     * @param serviceName 服务注册名
     * @return java.util.List<com.alibaba.nacos.api.naming.pojo.Instance>
     * @author tiiaan Email:tiiaan.w@gmail.com
     */
    public static List<Instance> getAllInstances(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }



    /**
     * 向Nacos注销服务
     * @param serviceName 服务注册名
     * @param inetSocketAddress 服务注册地址
     * @author tiiaan Email:tiiaan.w@gmail.com
     */
    public static void deregisterInstance(String serviceName, InetSocketAddress inetSocketAddress) {
        String host = getRegisterHost(inetSocketAddress);
        int port = getRegisterPort(inetSocketAddress);
        deregisterInstance(serviceName, host, port);
    }

    public static void deregisterInstance(String serviceName, String host, Integer port) {
        try {
            namingService.deregisterInstance(serviceName, host, port);
            log.info("服务 {}@{}:{} 注销成功", serviceName, host, port);
        } catch (NacosException e) {
            log.error("服务 {}@{}:{} 注销失败", serviceName, host, port, e);
            throw new MyRpcException(MyRpcError.SERVICE_DEREGISTER_FAILURE);
        }
    }


    /**
     * 向Nacos注销所有服务
     * @author tiiaan Email:tiiaan.w@gmail.com
     */
    public static void deregisterAllInstances() {
        if (registeredServices != null && !registeredServices.isEmpty() && registeredAddress != null) {
            log.info("正在注销服务");
            Iterator<String> iterator = registeredServices.iterator();
            while (iterator.hasNext()) {
                String serviceName = iterator.next();
                deregisterInstance(serviceName, registeredAddress);
            }
        }
    }


    /**
     * 向Nacos注册服务的地址, 提供两种选择: 1.IP(192.168.1.1); 2.HostName(tiiaandembp)
     * @param inetSocketAddress
     * @return java.lang.String
     * @author tiiaan Email:tiiaan.w@gmail.com
     */
    private static String getRegisterHost(InetSocketAddress inetSocketAddress) {
        if (USE_IP_MODE) {
            return inetSocketAddress.getAddress().getHostAddress();
        } else {
            return inetSocketAddress.getHostName();
        }
    }

    private static Integer getRegisterPort(InetSocketAddress inetSocketAddress) {
        return inetSocketAddress.getPort();
    }



}
