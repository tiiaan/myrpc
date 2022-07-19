package com.tiiaan.rpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Getter
@AllArgsConstructor
public enum ResponseStatus {

    SUCCESS(111, "方法调用成功"),
    FAIL(222, "方法调用失败"),
    METHOD_NOT_FOUND(333, "未找到指定方法"),
    CLASS_NOT_FOUND(444, "未找到指定类");


    private final int code;
    private final String message;

}
