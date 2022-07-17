package com.tiiaan.rpc.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 1.0
 * description
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyRpcRequest implements Serializable {

    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private String version;

    public String getServiceFullName() {
        return this.getInterfaceName() + this.getVersion();
    }

}
