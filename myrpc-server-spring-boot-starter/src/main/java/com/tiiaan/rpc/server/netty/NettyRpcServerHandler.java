package com.tiiaan.rpc.server.netty;

import com.tiiaan.rpc.common.entity.MyRpcRequest;
import com.tiiaan.rpc.common.entity.MyRpcResponse;
import com.tiiaan.rpc.handler.MyRpcRequestHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
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
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<MyRpcRequest> {

    @Resource
    private MyRpcRequestHandler myRpcRequestHandler;

    //static {
    //    myRpcRequestHandler = new MyRpcRequestHandler();
    //}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyRpcRequest msg) {
        try {
            log.info("服务端收到调用请求 {}", msg);
            Object returnObject = myRpcRequestHandler.handle(msg);
            ChannelFuture channelFuture = ctx.writeAndFlush(MyRpcResponse.success(returnObject, msg.getRequestId()));
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            log.error("", e);
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
