package com.tiiaan.rpc;

import com.tiiaan.rpc.json.JsonSerializer;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public interface MyRpcSerializer {

    byte[] serialize(Object obj);
    Object deserialize(byte[] bytes, Class<?> clazz);
    int getCode();

    static MyRpcSerializer getSerializer(int whichSerializer) {
        switch (whichSerializer) {
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }

}
