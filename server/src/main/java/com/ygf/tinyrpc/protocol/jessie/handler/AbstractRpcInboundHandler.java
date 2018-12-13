package com.ygf.tinyrpc.protocol.jessie.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

/**
 * 入站消息处理器(RpcInboundHandler和RpcChildInboundHandler)的抽象父类
 *
 * @author theo
 * @date 20181213
 */
public abstract class AbstractRpcInboundHandler extends ChannelInboundHandlerAdapter {
    /**
     * 获取对方的地址
     *
     * @param channel
     * @return
     */
    protected String getServerAddr(Channel channel) {
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String ip = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();
        return ip + ":" + port;
    }
}
