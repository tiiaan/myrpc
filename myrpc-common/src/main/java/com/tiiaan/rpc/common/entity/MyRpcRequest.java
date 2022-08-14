package com.tiiaan.rpc.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public String getServiceKey() {
        return this.getInterfaceName() + this.getVersion();
    }

}
