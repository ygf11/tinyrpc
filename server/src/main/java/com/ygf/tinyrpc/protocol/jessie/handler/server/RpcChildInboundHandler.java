package com.ygf.tinyrpc.protocol.jessie.handler.server;

import com.ygf.tinyrpc.protocol.jessie.message.Header;
import com.ygf.tinyrpc.protocol.jessie.message.InitSessionMessage;
import com.ygf.tinyrpc.rpc.server.RpcChildServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;
import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.CREATE_SESSION_REQUEST;

/**
 * provider端处理入站消息(读消息)的处理器
 *
 * @author theo
 * @date 20181213
 */
public class RpcChildInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(RpcChildInboundHandler.class);
    /**
     * rpcChildServer
     */
    private RpcChildServer server;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean isHeader = msg instanceof Header;
        if (!isHeader) {
            logger.warn("message {] is not a jessie header", msg);
            return;
        }

        Header header = (Header) msg;
        byte type = header.getType();
        switch (type) {
            case RPC_REQUEST:
                break;
            case EXIT_SESSION:
                break;
            case HEARTBEATS:
                break;
            case CREATE_SESSION_REQUEST:
                break;
            default:
                logger.error("not supported request type:{}", type);
        }
    }

    /**
     * 处理创建会话的请求
     *
     * @param header
     * @param addr
     */
    private void createSession(Header header, String addr) {
        boolean isInitSession = header instanceof InitSessionMessage;
        if (!isInitSession){
            logger.warn("msg is not a session init msg");
            return;
        }

        InitSessionMessage msg = (InitSessionMessage) header;
        //server.handleSessionInit(msg.getAppName());

        // TODO更新心跳
    }


}
