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
    UNKNOWN_HOST("未知host"),
    CONNECTION_FAILURE("连接服务端失败"),
    SERVER_START_FAILURE("服务启动失败"),
    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVER_NOT_FOUND("找不到对应的服务器"),
    UNKNOWN_PROTOCOL("不识别的协议包"),
    UNKNOWN_SERIALIZER("不识别的(反)序列化器"),
    UNKNOWN_PACKAGE_TYPE("不识别的数据包类型"),
    SERIALIZER_NOT_FOUND("找不到序列化器"),
    SERIALIZE_FAILURE("序列化失败"),
    DESERIALIZE_FAILURE("反序列化失败"),
    RESPONSE_NOT_MATCH("响应与请求号不匹配"),

    SERVICE_REGISTRY_CONNECTION_FAILURE("连接注册中心失败"),
    SERVICE_REGISTER_FAILURE("服务注册失败"),
    SERVICE_DEREGISTER_FAILURE("服务注销失败"),
    SERVICE_DISCOVERY_FAILURE("服务发现失败"),
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口"),

    IO_ERROR("IO操作异常"),
    REQUEST_FAILURE("请求发送失败");

    private final String message;

}
