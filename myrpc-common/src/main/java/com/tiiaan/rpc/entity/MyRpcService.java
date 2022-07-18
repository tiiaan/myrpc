package com.tiiaan.rpc.entity;

import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Data
@Builder
public class MyRpcService {

    private Object service;
    private String version = "";

    public MyRpcService() {
    }

    public MyRpcService(Object service) {
        this.service = service;
    }

    public MyRpcService(String version) {
        this(null, version);
    }

    public MyRpcService(Object service, String version) {
        this.version = version;
        this.service = service;
    }

    public String getServiceFullName() {
        return this.getServiceName() + this.version;
    }

    public String getServiceName() {
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces == null || interfaces.length == 0) {
            throw new MyRpcException(MyRpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }
}
