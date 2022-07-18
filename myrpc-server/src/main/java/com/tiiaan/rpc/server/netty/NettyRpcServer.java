package com.tiiaan.rpc.server.netty;

import com.tiiaan.rpc.entity.MyRpcService;
import com.tiiaan.rpc.factory.SingletonFactory;
import com.tiiaan.rpc.server.AbstractRpcServer;
import com.tiiaan.rpc.MyRpcDecoder;
import com.tiiaan.rpc.MyRpcEncoder;
import com.tiiaan.rpc.enums.MyRpcError;
import com.tiiaan.rpc.exception.MyRpcException;
import com.tiiaan.rpc.handler.NettyServerHandler;
import com.tiiaan.rpc.kryo.KryoSerializer;
import com.tiiaan.rpc.provider.ServiceProvider;
import com.tiiaan.rpc.provider.impl.ServiceProviderImpl;
import com.tiiaan.rpc.hook.MyRpcServerShutDownHook;
import com.tiiaan.rpc.server.MyRpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tiiaan Email:tiiaan.w@gmail.com
 * @version 0.0
 * description
 */

@Slf4j
public class NettyRpcServer extends AbstractRpcServer {


    private final ServiceProvider serviceProvider;

    public NettyRpcServer() {
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }


    @Override
    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //pipeline.addLast(new MyRpcEncoder(new JsonSerializer()));
                            pipeline.addLast(new MyRpcEncoder(new KryoSerializer()));
                            //pipeline.addLast(new MyRpcEncoder(new HessianSerializer()));
                            pipeline.addLast(new MyRpcDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
            //优雅关闭钩子
            MyRpcServerShutDownHook.getShutDownHook().start();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("服务启动失败", e);
            throw new MyRpcException(MyRpcError.SERVER_START_FAILURE);
        } finally {
            log.info("Netty 服务正在关闭");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    @Override
    public void register(Object service, String version) {
        serviceProvider.publishService(new MyRpcService(service, version));
    }

}