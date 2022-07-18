package com.tiiaan.rpc.serialize;

import com.tiiaan.rpc.MySPI;
import com.tiiaan.rpc.serialize.hessian.HessianSerialize;
import com.tiiaan.rpc.serialize.json.JsonSerialize;
import com.tiiaan.rpc.serialize.kryo.KryoSerialize;

import java.io.IOException;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@MySPI
public interface MyRpcSerialize {

    byte[] serialize(Object obj) throws IOException;
    Object deserialize(byte[] bytes, Class<?> clazz);
    int getCode();

    static MyRpcSerialize getSerializer(int whichSerializer) {
        switch (whichSerializer) {
            case 0:
                return new KryoSerialize();
            case 1:
                return new JsonSerialize();
            case 2:
                return new HessianSerialize();
            default:
                return null;
        }
    }

}
