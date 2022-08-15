package com.tiiaan.rpc.client.netty;

import com.tiiaan.rpc.bean.ApplicationContextUtils;
import com.tiiaan.rpc.client.MyRpcClient;
import com.tiiaan.rpc.codec.MyRpcDecoder;
import com.tiiaan.rpc.codec.MyRpcEncoder;
import com.tiiaan.rpc.common.entity.MyRpcRequest;
import com.tiiaan.rpc.common.entity.MyRpcResponse;
import com.tiiaan.rpc.common.entity.MyRpcResult;
import com.tiiaan.rpc.registry.MyRpcServiceDiscovery;
import com.tiiaan.rpc.serialize.kryo.KryoSerialize;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
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


    @Resource
    private MyRpcServiceDiscovery myRpcServiceDiscovery;
    @Resource
    private FuturesHolder futuresHolder;
    @Resource
    private ChannelProvider channelProvider;


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
                        pipeline.addLast(ApplicationContextUtils.autowire(new NettyRpcClientHandler()));
                    }
                });
        //myRpcServiceDiscovery = ExtensionLoader.getExtensionLoader(MyRpcServiceDiscovery.class).getExtension(Constants.DEFAULT_REGISTRY);
        //this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        //this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }


    @Override
    public Object sendRequest(MyRpcRequest myRpcRequest) {
        for (;;) {
            MyRpcResult myRpcResult = doSendRequest(myRpcRequest);
            if (myRpcResult.isSuccess()) {
                return myRpcResult.getReturnObject();
            }
        }
    }


    private MyRpcResult doSendRequest(MyRpcRequest myRpcRequest) {
        InetSocketAddress inetSocketAddress = myRpcServiceDiscovery.lookup(myRpcRequest.getServiceKey());
        if (inetSocketAddress == null) {
            log.error("找不到可用节点 [{}]", myRpcRequest.getServiceKey());
            return new MyRpcResult(false, null);
        }
        Channel channel = getChannel(inetSocketAddress);
        if (channel == null) {
            log.error("节点已过期 [{}]", inetSocketAddress);
            return new MyRpcResult(false, null);
        }
        CompletableFuture<MyRpcResponse<Object>> completableFuture = new CompletableFuture<>();
        if (channel.isActive()) {
            futuresHolder.put(myRpcRequest.getRequestId(), completableFuture);
            channel.writeAndFlush(myRpcRequest).addListener((ChannelFutureListener) channelFuture -> {
                if(channelFuture.isSuccess()) {
                    log.info("发送请求 [{}]", myRpcRequest.getRequestId());
                } else {
                    channelFuture.channel().close();
                    completableFuture.completeExceptionally(channelFuture.cause());
                    log.error("请求发送失败 [{}]", myRpcRequest.getRequestId(), channelFuture.cause());
                }
            });
        }
        return new MyRpcResult(true, completableFuture);
    }


    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.getChannel(inetSocketAddress);
        if (channel == null) {
            try {
                channel = doConnect(inetSocketAddress);
                channelProvider.addChannel(inetSocketAddress, channel);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return channel;
    }


    public Channel doConnect(InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                log.info("连接节点 [{}]", inetSocketAddress);
                completableFuture.complete(channelFuture.channel());
            } else {
                completableFuture.complete(null);
                //throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }


    public void close() {
        group.shutdownGracefully();
    }

}
