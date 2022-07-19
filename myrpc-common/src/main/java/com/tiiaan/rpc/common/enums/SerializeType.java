package com.tiiaan.rpc.common.enums;

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

    KRYO((byte) 0x01, "kryo"),
    HESSIAN((byte) 0x02, "hessian"),
    JSON((byte) 0x03, "json");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (SerializeType serializeType : SerializeType.values()) {
            if (serializeType.getCode() == code) {
                return serializeType.getName();
            }
        }
        return null;
    }
}
