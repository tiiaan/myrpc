package com.tiiaan.rpc.netty;

import com.tiiaan.rpc.AbstractRpcClient;
import com.tiiaan.rpc.MyRpcDecoder;
import com.tiiaan.rpc.MyRpcEncoder;
import com.tiiaan.rpc.entity.MyRpcRequest;
import com.tiiaan.rpc.entity.MyRpcResponse;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import com.tiiaan.rpc.handler.NettyClientHandler;
import com.tiiaan.rpc.json.JsonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class NettyRpcClient extends AbstractRpcClient {

    private static final Bootstrap bootstrap;

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MyRpcDecoder());
                        pipeline.addLast(new MyRpcEncoder(new JsonSerializer()));
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }


    @Override
    public Object sendRequest(MyRpcRequest myRpcRequest) {
        try {
            ChannelFuture future = bootstrap.connect(serverHost, serverPort).sync();
            log.info("连接到服务器 {}:{}", serverHost, serverPort);
            Channel channel = future.channel();
            if(channel != null) {
                channel.writeAndFlush(myRpcRequest).addListener(future1 -> {
                    if(future1.isSuccess()) {
                        log.info("发送 RPC 调用请求 {}", myRpcRequest);
                    } else {
                        log.error("请求发送失败", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<MyRpcResponse> key = AttributeKey.valueOf("rpcResponse");
                return channel.attr(key).get();
            }
        } catch (InterruptedException e) {
            log.error("请求发送失败", e);
            throw new MyRpcException(MyRpcError.REQUEST_FAILURE);
        }
        return null;
    }

}
