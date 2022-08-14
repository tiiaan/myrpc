package com.tiiaan.rpc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Getter
@Setter
@ConfigurationProperties("myrpc.server")
public class MyRpcServerProperties {

    private Integer port;
    private String address;

}
