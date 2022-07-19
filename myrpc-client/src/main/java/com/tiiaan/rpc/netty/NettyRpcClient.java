package com.tiiaan.rpc.netty;

import com.tiiaan.rpc.*;
import com.tiiaan.rpc.common.constants.Constants;
import com.tiiaan.rpc.common.entity.MyRpcRequest;
import com.tiiaan.rpc.common.entity.MyRpcResponse;
import com.tiiaan.rpc.common.factory.SingletonFactory;
import com.tiiaan.rpc.handler.NettyClientHandler;
import com.tiiaan.rpc.registry.MyRpcServiceDiscovery;
import com.tiiaan.rpc.serialize.kryo.KryoSerialize;
import com.tiiaan.rpc.spi.ExtensionLoader;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class NettyRpcClient implements MyRpcClient {

    private final Bootstrap bootstrap;
    private final EventLoopGroup group;
    private final MyRpcServiceDiscovery myRpcServiceDiscovery;
    private final UnprocessedRequests unprocessedRequests;
    private final NettyChannel nettyChannel;


    public NettyRpcClient() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //是否开启 TCP 底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MyRpcDecoder());
                        //pipeline.addLast(new MyRpcEncoder(new JsonSerializer()));
                        pipeline.addLast(new MyRpcEncoder(new KryoSerialize()));
                        //pipeline.addLast(new MyRpcEncoder(new HessianSerializer()));
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
        myRpcServiceDiscovery = ExtensionLoader.getExtensionLoader(MyRpcServiceDiscovery.class).getExtension(Constants.DEFAULT_REGISTRY);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.nettyChannel = SingletonFactory.getInstance(NettyChannel.class);
    }


    @Override
    public Object sendRequest(MyRpcRequest myRpcRequest) {
        CompletableFuture<MyRpcResponse<Object>> completableFuture = new CompletableFuture<>();
        try {
            InetSocketAddress inetSocketAddress = myRpcServiceDiscovery.lookup(myRpcRequest.getServiceKey());
            Channel channel = getChannel(inetSocketAddress);
            if (channel.isActive()) {
                unprocessedRequests.put(myRpcRequest.getRequestId(), completableFuture);
                channel.writeAndFlush(myRpcRequest).addListener((ChannelFutureListener) channelFuture -> {
                    if(channelFuture.isSuccess()) {
                        log.info("请求发送成功 [{}]", myRpcRequest);
                    } else {
                        channelFuture.channel().close();
                        completableFuture.completeExceptionally(channelFuture.cause());
                        log.error("请求发送失败", channelFuture.cause());
                    }
                });
            }
        } catch (InterruptedException | ExecutionException e) {
            unprocessedRequests.remove(myRpcRequest.getRequestId());
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return completableFuture;
    }


    public Channel getChannel(InetSocketAddress inetSocketAddress) throws InterruptedException, ExecutionException {
        Channel channel = nettyChannel.getChannel(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            nettyChannel.addChannel(inetSocketAddress, channel);
        }
        return channel;
    }


    public Channel doConnect(InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                log.info("连接服务提供者 [{}:{}]", inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
                completableFuture.complete(channelFuture.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }


    public void close() {
        group.shutdownGracefully();
    }

}
