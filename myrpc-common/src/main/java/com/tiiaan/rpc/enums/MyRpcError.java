package com.tiiaan.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Getter
@AllArgsConstructor
public enum MyRpcError {

    UNKNOWN_ERROR("未知错误"),
    CONNECTION_FAILURE("连接服务端失败"),
    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口"),
    UNKNOWN_PROTOCOL("不识别的协议包"),
    UNKNOWN_SERIALIZER("不识别的(反)序列化器"),
    UNKNOWN_PACKAGE_TYPE("不识别的数据包类型"),
    SERIALIZER_NOT_FOUND("找不到序列化器"),
    RESPONSE_NOT_MATCH("响应与请求号不匹配"),
    SERVICE_REGISTRY_CONNECTION_FAILURE("连接注册中心失败"),
    SERVICE_REGISTER_FAILURE("服务注册失败");

    private final String message;

}
