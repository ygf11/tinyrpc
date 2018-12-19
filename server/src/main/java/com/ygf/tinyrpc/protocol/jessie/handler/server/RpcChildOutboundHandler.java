package com.ygf.tinyrpc.protocol.jessie.handler.server;

import com.ygf.tinyrpc.common.RpcResult;
import com.ygf.tinyrpc.protocol.jessie.message.Header;
import com.ygf.tinyrpc.protocol.jessie.message.RpcResponseMessage;
import com.ygf.tinyrpc.rpc.OutboundMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

/**
 * 服务器端处理出站(写)消息的处理器
 *
 * @author theo
 * @date 20181212
 */
public class RpcChildOutboundHandler extends MessageToMessageEncoder<OutboundMsg> {
    private static final Logger logger = LoggerFactory.getLogger(RpcChildOutboundHandler.class);

    public RpcChildOutboundHandler() {
        super();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, OutboundMsg msg, List<Object> out) throws Exception {
        byte type = msg.getType();
        switch (type) {
            case HEARTBEATS:
                break;
            case RPC_RESPONSE:
                rpcResponse(msg, out);
                break;
            case CREATE_SESSION_RESPONSE:
                sessionResponse(msg, out);
                break;
            default:
                logger.error("outbound type: {}, jessie not support", type);
        }
    }

    private void heartBeats() {

    }

    private void rpcResponse() {

    }

    /**
     * 编码session响应消息
     *
     * @param msg
     * @param out
     */
    private void sessionResponse(OutboundMsg msg, List<Object> out) {
        boolean isInteger = msg.getArg() instanceof Integer;
        if (!isInteger) {
            logger.error("outbound type and args not matched");
            return;
        }
        Integer sessionId = (Integer) msg.getArg();
        Header header = new Header();
        header.setProtocol(PROTOCOL);
        header.setVersion(CURRENT_VERSION);
        header.setSessionId(sessionId);
        header.setType(CREATE_SESSION_RESPONSE);

        out.add(header);
    }

    /**
     * 编码rpc响应消息
     *
     * @param msg
     * @param out
     */
    private void rpcResponse(OutboundMsg msg, List<Object> out) {
        Object args = msg.getArg();
        boolean isRpc = args instanceof RpcResult;
        if (!isRpc) {
            logger.error("outbound type and args not matched");
            return;
        }

        RpcResult result = (RpcResult) args;
        RpcResponseMessage header = new RpcResponseMessage();
        header.setRequestId(result.getRequestId());
        //header.setResultType(NORMAL);

    }
}
