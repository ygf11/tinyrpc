package com.ygf.tinyrpc.rpc.client;

import com.ygf.tinyrpc.protocol.jessie.code.ByteToMsgDecoder;
import com.ygf.tinyrpc.protocol.jessie.code.MsgToByteEncoder;
import com.ygf.tinyrpc.protocol.jessie.handler.client.RpcInboundHandler;
import com.ygf.tinyrpc.protocol.jessie.handler.client.RpcOutboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端网路连接启动类
 * // TODO 异常处理和关闭操作
 *
 * @author theo
 * @date 20181211
 */
public class RpcConnector {
    /**
     * 保存ip到对应连接的映射  用于解决单连接问题
     */
    private static Map<String, RpcConnector> connectorMap = new ConcurrentHashMap<String, RpcConnector>();
    /**
     * promise
     */
    private ChannelFuture future;
    /**
     * 处理网络i/o的线程池
     */
    private EventLoopGroup worker;
    /**
     * 服务器地址
     */
    private InetSocketAddress addr;
    /**
     * rpcClient
     */
    private RpcClient client;

    public RpcConnector(InetSocketAddress addr){
        this.addr = addr;
    }

    /**
     * 进行连接
     * @throws Exception
     */
    public void connect() throws Exception{
        worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .remoteAddress(addr)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.TCP_NODELAY, false)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel sc){
                        ChannelPipeline pipeline = sc.pipeline();
                        // 出站
                        pipeline.addLast(new RpcOutboundHandler());
                        pipeline.addLast(new MsgToByteEncoder());
                        // 入站
                        pipeline.addLast(new RpcInboundHandler(client));
                        pipeline.addLast(new ByteToMsgDecoder());
                    }
                });
        future = bootstrap.connect().sync();
    }
}
