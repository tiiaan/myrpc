package com.tiiaan.rpc.client.netty;

import com.tiiaan.rpc.common.entity.MyRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<MyRpcResponse> {

    @Resource
    private FuturesHolder futuresHolder;

    //public NettyRpcClientHandler() {
    //    this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    //}


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyRpcResponse msg) throws Exception {
        try {
            futuresHolder.complete(msg);
            log.info("接收响应 [{}]", msg.getRequestId());
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程中发现异常", cause);
        ctx.close();
    }

}
