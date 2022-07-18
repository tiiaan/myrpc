package com.tiiaan.rpc.netty;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

public class NettyChannel {

    private Map<String, Channel> channelMap = new ConcurrentHashMap<>();


    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        String address = inetSocketAddress.toString();
        if (channelMap.containsKey(address)) {
            Channel channel = channelMap.get(address);
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(address);
            }
        }
        return null;
    }


    public void addChannel(InetSocketAddress inetSocketAddress, Channel channel) {
        if (inetSocketAddress != null && channel != null) {
            channelMap.put(inetSocketAddress.toString(), channel);
        }
    }

}
