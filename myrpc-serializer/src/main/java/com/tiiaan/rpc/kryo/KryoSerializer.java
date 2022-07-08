package com.tiiaan.rpc.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.tiiaan.rpc.MyRpcSerializer;
import com.tiiaan.rpc.entity.MyRpcRequest;
import com.tiiaan.rpc.entity.MyRpcResponse;
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
public class KryoSerializer implements MyRpcSerializer {

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(MyRpcResponse.class);
        kryo.register(MyRpcRequest.class);
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return kryo;
    });


    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream);) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            kryo.writeObject(output, obj);
            return output.toBytes();
        } catch (IOException e) {
            log.error("序列化失败", e);
            throw new MyRpcException(MyRpcError.SERIALIZE_FAILURE);
        } finally {
            KRYO_THREAD_LOCAL.remove();
        }
    }


    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream);) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            return kryo.readObject(input, clazz);
        } catch (IOException e) {
            log.error("反序列化失败", e);
            throw new MyRpcException(MyRpcError.DESERIALIZE_FAILURE);
        } finally {
            KRYO_THREAD_LOCAL.remove();
        }
    }


    @Override
    public int getCode() {
        return SerializerCode.KRYO.getCode();
    }

}
