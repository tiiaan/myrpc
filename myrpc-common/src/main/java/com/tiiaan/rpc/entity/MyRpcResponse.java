package com.tiiaan.rpc.entity;

import com.tiiaan.rpc.enums.ResponseStatus;
import lombok.*;

import java.io.Serializable;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 1.0
 * description
 */

@Data
@NoArgsConstructor
public class MyRpcResponse<T> implements Serializable {

    private Integer statusCode;
    private String message;
    private T data;

    public static <T> MyRpcResponse<T> success(T data) {
        MyRpcResponse<T> myRpcResponse = new MyRpcResponse<>();
        myRpcResponse.setStatusCode(ResponseStatus.SUCCESS.getCode());
        myRpcResponse.setData(data);
        return myRpcResponse;
    }

    public static <T> MyRpcResponse<T> fail(ResponseStatus status) {
        MyRpcResponse<T> myRpcResponse = new MyRpcResponse<>();
        myRpcResponse.setStatusCode(status.getCode());
        myRpcResponse.setMessage(status.getMessage());
        return myRpcResponse;
    }

}
