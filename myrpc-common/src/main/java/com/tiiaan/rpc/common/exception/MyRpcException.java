package com.tiiaan.rpc.common.exception;

import com.tiiaan.rpc.common.enums.MyRpcError;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */


public class MyRpcException extends RuntimeException {

    public MyRpcException(MyRpcError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }

    public MyRpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyRpcException(MyRpcError error) {
        super(error.getMessage());
    }

}
