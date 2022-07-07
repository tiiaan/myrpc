package com.tiiaan.rpc.util;

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

}
