package com.tiiaan.rpc.common.config;

import lombok.Getter;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Getter
public class MyRpcServerProperties {
    private Integer port = 9000;
    private String registryAddr = "127.0.0.1:8848";
}
