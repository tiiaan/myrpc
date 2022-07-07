package com.tiiaan.rpc.handler;

import com.tiiaan.rpc.entity.MyRpcRequest;
import com.tiiaan.rpc.entity.MyRpcResponse;
import com.tiiaan.rpc.provider.ServiceProvider;
import com.tiiaan.rpc.provider.impl.ServiceProviderImpl;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<MyRpcRequest> {

    private static MyRpcRequestHandler myRpcRequestHandler;

    static {
        myRpcRequestHandler = new MyRpcRequestHandler();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyRpcRequest msg) throws Exception {
        try {
            log.info("服务端收到调用请求 {}", msg);
            Object returnObject = myRpcRequestHandler.handle(msg);
            ChannelFuture channelFuture = ctx.writeAndFlush(MyRpcResponse.success(returnObject));
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
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
