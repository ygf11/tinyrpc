package com.ygf.tinyrpc.protocol.dubbo.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * dubbo消息解码器，可以将byte[]转换为message
 * @author theo
 * @date 20181129
 */
public class DubboByteToMessageDecoder extends ByteToMessageDecoder{

    private static final Logger logger = LoggerFactory.getLogger(DubboByteToMessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }
}
