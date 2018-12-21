package com.ygf.tinyrpc.rpc.server;

import com.ygf.tinyrpc.protocol.jessie.code.ByteToMsgDecoder;
import com.ygf.tinyrpc.protocol.jessie.code.MsgToByteEncoder;
import com.ygf.tinyrpc.protocol.jessie.handler.server.RpcChildInboundHandler;
import com.ygf.tinyrpc.protocol.jessie.handler.server.RpcChildOutboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 服务提供端监听网络连接的启动类
 * //TODO 关闭监听
 *
 * @author theo
 * @date 20181220
 */
public class RpcServerConnector {
    private static Logger logger = LoggerFactory.getLogger(RpcServerConnector.class);
    /**
     * 监听网络连接的线程池
     */
    private EventLoopGroup boss;
    /**
     * 处理会话/rpc请求/心跳的线程池
     */
    private EventLoopGroup worker;
    /**
     * channelPromise
     */
    private ChannelPromise promise;
    /**
     * 监听的网络地址
     */
    private InetSocketAddress addr;
    /**
     * server
     */
    private RpcChildServer server;

    public RpcServerConnector(InetSocketAddress addr){
        this.addr = addr;
    }

    /**
     * 开始监听网络
     */
    public void bootStrap(){
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                .localAddress(addr)
                .option(ChannelOption.SO_BACKLOG, 100)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.TCP_NODELAY, false)
                .childOption(ChannelOption.TCP_NODELAY, false)
                .childOption(ChannelOption.SO_KEEPALIVE, false)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel sc){
                        ChannelPipeline pipeline = sc.pipeline();
                        // 出战
                        pipeline.addLast(new RpcChildOutboundHandler());
                        pipeline.addLast(new MsgToByteEncoder());
                        // 入站
                        pipeline.addLast(new RpcChildInboundHandler(server));
                        pipeline.addLast(new ByteToMsgDecoder());

                    }
                });
        // 开始监听网络
        bootstrap.bind();
    }

    /**
     * 结束网络监听
     *
     * @throws Exception
     */
    public void shutDown() throws Exception{
        try {
            promise.channel().closeFuture().sync();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
