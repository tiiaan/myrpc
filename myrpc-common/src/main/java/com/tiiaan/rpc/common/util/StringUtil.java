package com.tiiaan.rpc.common.util;

import java.net.InetSocketAddress;
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

    public static InetSocketAddress buildAddr(String address) {
        int index = address.lastIndexOf(':');
        String host;
        int port = 0;
        if (index >= 0) {
            host = address.substring(0, index);
            port = Integer.parseInt(address.substring(index + 1));
        } else {
            host = address;
        }
        return new InetSocketAddress(host, port);
    }

}
