package com.tiiaan.rpc.serialize.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiiaan.rpc.serialize.MyRpcSerialize;
import com.tiiaan.rpc.common.entity.MyRpcRequest;
import com.tiiaan.rpc.common.entity.MyRpcResponse;
import com.tiiaan.rpc.common.enums.MyRpcError;
import com.tiiaan.rpc.common.exception.MyRpcException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class JsonSerialize implements MyRpcSerialize {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("序列化失败", e);
            throw new MyRpcException(MyRpcError.SERIALIZE_FAILURE);
        }
    }


    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            Object obj = objectMapper.readValue(bytes, clazz);
            if (obj instanceof MyRpcRequest) {
                obj = handleMyRpcRequest(obj);
            }
            if (obj instanceof MyRpcResponse) {
                obj = handleMyRpcResponse(obj);
            }
            return obj;
        } catch (IOException e) {
            log.error("反序列化失败", e);
            throw new MyRpcException(MyRpcError.DESERIALIZE_FAILURE);
        }
    }


    private Object handleMyRpcRequest(Object obj) throws IOException {
        MyRpcRequest myRpcRequest = (MyRpcRequest) obj;
        for (int i = 0; i < myRpcRequest.getParamTypes().length; i++) {
            Class<?> clazz = myRpcRequest.getParamTypes()[i];
            if (!clazz.isAssignableFrom(myRpcRequest.getParameters()[i].getClass())) {
                byte[] bytes = objectMapper.writeValueAsBytes(myRpcRequest.getParameters()[i]);
                myRpcRequest.getParameters()[i] = objectMapper.readValue(bytes, clazz);
            }
        }
        return myRpcRequest;
    }


    private Object handleMyRpcResponse(Object obj) throws IOException {
        MyRpcResponse myRpcResponse = (MyRpcResponse) obj;
        byte[] bytes = objectMapper.writeValueAsBytes(myRpcResponse.getData());
        //myRpcResponse.setData(objectMapper.readValue(bytes, MyMessage.class));
        return myRpcResponse;
    }


    @Override
    public int getCode() {
        return 1;
    }
}
