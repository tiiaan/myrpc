package com.tiiaan.rpc.handler;

import com.tiiaan.rpc.entity.MyRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<MyRpcResponse> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyRpcResponse msg) throws Exception {
        try {
            log.info("客户端收到调用结果 {}", msg);
            AttributeKey<MyRpcResponse> key = AttributeKey.valueOf("rpcResponse");
            ctx.channel().attr(key).set(msg);
            ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程中发现异常", cause);
        cause.printStackTrace();
        ctx.close();
    }

}
