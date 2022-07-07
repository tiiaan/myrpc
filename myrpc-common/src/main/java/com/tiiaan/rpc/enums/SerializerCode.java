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
public enum SerializerCode {

    JSON(1);

    private final int code;

}
