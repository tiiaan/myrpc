package com.tiiaan.rpc.common.entity;

import com.tiiaan.rpc.common.enums.ResponseStatus;
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
public class MyRpcResponse<T> implements Serializable {

    private String requestId;
    private Integer statusCode;
    private String message;
    private T data;

    public static <T> MyRpcResponse<T> success(T data, String requestId) {
        MyRpcResponse<T> myRpcResponse = new MyRpcResponse<>();
        myRpcResponse.setStatusCode(ResponseStatus.SUCCESS.getCode());
        myRpcResponse.setData(data);
        myRpcResponse.setRequestId(requestId);
        return myRpcResponse;
    }

    public static <T> MyRpcResponse<T> fail(ResponseStatus status) {
        MyRpcResponse<T> myRpcResponse = new MyRpcResponse<>();
        myRpcResponse.setStatusCode(status.getCode());
        myRpcResponse.setMessage(status.getMessage());
        return myRpcResponse;
    }

}
