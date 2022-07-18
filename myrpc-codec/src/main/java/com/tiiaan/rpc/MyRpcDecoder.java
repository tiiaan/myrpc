package com.tiiaan.rpc;

import com.tiiaan.rpc.entity.MyRpcRequest;
import com.tiiaan.rpc.entity.MyRpcResponse;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.enums.PackageType;
import com.tiiaan.rpc.exception.MyRpcException;
import com.tiiaan.rpc.serialize.MyRpcSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class MyRpcDecoder extends ReplayingDecoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magic = in.readInt();
        if (magic != MAGIC_NUMBER) {
            log.error("不识别的协议包 {}", magic);
            throw new MyRpcException(MyRpcError.UNKNOWN_PROTOCOL);
        }
        int packageCode = in.readInt();
        Class<?> packageClass;
        if (packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = MyRpcRequest.class;
        } else if (packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = MyRpcResponse.class;
        } else {
            log.error("不识别的数据包 {}", packageCode);
            throw new MyRpcException(MyRpcError.UNKNOWN_PACKAGE_TYPE);
        }
        int serializerCode = in.readInt();
        MyRpcSerialize serializer = MyRpcSerialize.getSerializer(serializerCode);
        if (serializer == null) {
            log.error("不识别的序列化器 {}", serializerCode);
            throw new MyRpcException(MyRpcError.UNKNOWN_SERIALIZER);
        }
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        Object obj = serializer.deserialize(bytes, packageClass);
        out.add(obj);
    }


}
