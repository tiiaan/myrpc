package com.tiiaan.rpc.netty;

import com.tiiaan.rpc.*;
import com.tiiaan.rpc.entity.MyRpcRequest;
import com.tiiaan.rpc.entity.MyRpcResponse;
import com.tiiaan.rpc.factory.SingletonFactory;
import com.tiiaan.rpc.handler.NettyClientHandler;
import com.tiiaan.rpc.hessian.HessianSerializer;
import com.tiiaan.rpc.kryo.KryoSerializer;
import com.tiiaan.rpc.nacos.NacosServiceDiscovery;
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
    private final ServiceDiscovery serviceDiscovery;
    private final UnprocessedRequests unprocessedRequests;
    private final ChannelCache channelCache;


    public NettyRpcClient() {
        //serviceDiscovery = new NacosServiceDiscovery();
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
                        pipeline.addLast(new MyRpcEncoder(new KryoSerializer()));
                        //pipeline.addLast(new MyRpcEncoder(new HessianSerializer()));
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
        this.serviceDiscovery = SingletonFactory.getInstance(NacosServiceDiscovery.class);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.channelCache = SingletonFactory.getInstance(ChannelCache.class);
    }


    @Override
    public Object sendRequest(MyRpcRequest myRpcRequest) {
        CompletableFuture<MyRpcResponse> completableFuture = new CompletableFuture<>();
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(myRpcRequest.getInterfaceName());
            Channel channel = getChannel(inetSocketAddress);

            //if (!channel.isActive()) {
            //    log.info("Netty 服务正在关闭...");
            //    group.shutdownGracefully();
            //    return null;
            //}

            if (channel.isActive()) {
                unprocessedRequests.put(myRpcRequest.getRequestId(), completableFuture);
                channel.writeAndFlush(myRpcRequest).addListener((ChannelFutureListener) future1 -> {
                    if(future1.isSuccess()) {
                        log.info("发送调用请求, {}", myRpcRequest);
                    } else {
                        future1.channel().close();
                        completableFuture.completeExceptionally(future1.cause());
                        log.error("请求发送失败", future1.cause());
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
        Channel channel = channelCache.getChannel(inetSocketAddress);
        if (channel == null) {
            channel = createChannel(inetSocketAddress);
            channelCache.setChannel(inetSocketAddress, channel);
        }
        return channel;
    }


    public Channel createChannel(InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future1 -> {
            if (future1.isSuccess()) {
                log.info("连接服务提供者 {}:{}", inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
                completableFuture.complete(future1.channel());
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
