package com.ygf.tinyrpc.protocol.handler;

import com.ygf.tinyrpc.protocol.client.ClientMain;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * echo handler
 * @author theo
 * @Date 20181127
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter{

    public final ByteBuf firstMessage;

    public EchoClientHandler(){
        firstMessage = Unpooled.buffer(ClientMain.SIZE);
        for (int i = 0; i < ClientMain.SIZE; ++i){
            firstMessage.writeByte((byte)i);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
