package com.tiiaan.rpc.util;

import java.util.UUID;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public class StringUtil {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
