package com.ygf.tinyrpc.protocol.jessie.handler.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * provider端处理入站消息(读消息)的处理器
 *
 * @author theo
 * @date 20181213
 */
public class RpcChildInboundHandler extends MessageToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {

    }
}
