package ygf.tinyrpc.rpc.server;

import ygf.tinyrpc.protocol.jessie.code.ByteToMsgDecoder;
import ygf.tinyrpc.protocol.jessie.code.MsgToByteEncoder;
import ygf.tinyrpc.protocol.jessie.handler.server.RpcChildInboundHandler;
import ygf.tinyrpc.protocol.jessie.handler.server.RpcChildOutboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 服务提供端监听网络连接的启动类(单例)
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
    private ChannelFuture future;
    /**
     * 监听的网络地址
     */
    private String ip;
    /**
     * 监听端口
     */
    private Integer port;
    /**
     * server
     */
    private RpcChildServer server;

    public RpcServerConnector(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
        this.server = new RpcChildServer();
    }

    /**
     * 开始监听网络
     */
    public void bootStrap() {
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                // TODO 多网卡
                .localAddress(new InetSocketAddress(ip, port))
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.TCP_NODELAY, false)
                .childOption(ChannelOption.TCP_NODELAY, false)
                .childOption(ChannelOption.SO_KEEPALIVE, false)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel sc) {
                                ChannelPipeline pipeline = sc.pipeline();

                                pipeline.addLast(new ByteToMsgDecoder());
                                pipeline.addLast(new MsgToByteEncoder());

                                pipeline.addLast(new RpcChildOutboundHandler());
                                pipeline.addLast(new RpcChildInboundHandler(server));

                            }
                        });
        ;
        // 开始监听网络
        try {
            future = bootstrap.bind().sync();
        } catch (
                InterruptedException e) {

        }

    }

    /**
     * 结束网络监听
     *
     * @throws Exception
     */
    public void shutDown() throws Exception {
        try {
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    /**
     * 完成最后的暴露工作
     *
     * @param service
     * @param ref
     */
    public void completeExport(String service, Object ref) {
        server.addExported(service, ref);
    }
}
