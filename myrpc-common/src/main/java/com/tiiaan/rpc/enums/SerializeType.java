package com.tiiaan.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@AllArgsConstructor
@Getter
public enum SerializeType {

    KRYO((byte) 0x01, "KRYO"),
    HESSIAN((byte) 0x02, "HESSIAN"),
    JSON((byte) 0x03, "JSON");

    private final byte code;
    private final String name;

    public static String getSerializerName(byte code) {
        for (SerializeType serializeType : SerializeType.values()) {
            if (serializeType.getCode() == code) {
                return serializeType.getName();
            }
        }
        return null;
    }

}
