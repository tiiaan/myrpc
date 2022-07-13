package com.tiiaan.rpc.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.tiiaan.rpc.MyRpcSerializer;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.enums.SerializerCode;
import com.tiiaan.rpc.exception.MyRpcException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class HessianSerializer implements MyRpcSerializer {


    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("序列化失败", e);
            throw new MyRpcException(MyRpcError.SERIALIZE_FAILURE);
        }
    }


    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            HessianInput hessianInput = new HessianInput(byteArrayInputStream);
            Object obj = hessianInput.readObject();
            return clazz.cast(obj);
        } catch (IOException e) {
            log.error("序列化失败", e);
            throw new MyRpcException(MyRpcError.SERIALIZE_FAILURE);
        }
    }


    @Override
    public int getCode() {
        return SerializerCode.HESSIAN.getCode();
    }

}
