package com.tiiaan.rpc;

import com.tiiaan.rpc.json.JsonSerializer;
import com.tiiaan.rpc.kryo.KryoSerializer;
import org.checkerframework.checker.units.qual.K;

import java.io.IOException;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public interface MyRpcSerializer {

    byte[] serialize(Object obj) throws IOException;
    Object deserialize(byte[] bytes, Class<?> clazz);
    int getCode();

    static MyRpcSerializer getSerializer(int whichSerializer) {
        switch (whichSerializer) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }

}
