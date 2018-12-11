package com.ygf.tinyrpc.protocol.jessie.handler.client;

import com.ygf.tinyrpc.common.RpcInvocation;
import com.ygf.tinyrpc.protocol.jessie.message.InitSessionMessage;
import com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol;
import com.ygf.tinyrpc.protocol.jessie.message.RpcRequestMessage;
import com.ygf.tinyrpc.rpc.OutboundMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 将出站请求转化为编码器可以编码的请求对象
 * TODO
 *
 * @author theo
 * @date 20181208
 */
public class RpcOutboundHandler extends MessageToMessageEncoder<OutboundMsg> {

    private static final Logger logger = LoggerFactory.getLogger(RpcOutboundHandler.class);
    /**
     * 连接对应的服务
     */
    private Class service;

    public RpcOutboundHandler(Class service){
        super();
        this.service = service;
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, OutboundMsg msg, List<Object> out) throws Exception {
        switch (msg.getType()) {
            case CREATE_SESSION_REQUEST:
                createSession(msg, out);
                break;
            case EXIT_SESSION:
                exitSession(msg, out);
                break;
            case RPC_REQUEST:
                rpcRequest(msg, out);
                break;
            case HEARTBEATS:

                break;
            default:
                logger.error("jessie not supported outbound type {}", msg.getType());
        }
    }

    /**
     * 发送会话请求
     *
     * @param msg
     * @param out
     */
    public void createSession(OutboundMsg msg, List<Object> out) {
        Object arg = msg.getArg();
        boolean isAppName = arg instanceof String;
        if (!isAppName) {
            logger.error("type {} not matched arg {}", msg.getType(), arg);
            return;
        }

        final InitSessionMessage req = new InitSessionMessage();
        req.setProtocol(PROTOCOL);
        req.setVersion(CURRENT_VERSION);
        req.setType(CREATE_SESSION_REQUEST);
        req.setSessionId(0);
        req.setAppName((String) arg);

        out.add(req);
    }

    /**
     * 发送rpc请求
     *
     * @param msg
     * @param out
     */
    private void rpcRequest(OutboundMsg msg, List<Object> out) {
        Object arg = msg.getArg();
        boolean isInvoker = arg instanceof RpcInvocation;
        if (!isInvoker) {
            logger.error("type {} not matched arg {}", msg.getType(), arg);
            return;
        }
        // rpc请求参数
        RpcInvocation invocation = (RpcInvocation) arg;
        RpcRequestMessage req = new RpcRequestMessage();
        req.setProtocol(JessieProtocol.PROTOCOL);
        req.setVersion(JessieProtocol.CURRENT_VERSION);
        req.setType(JessieProtocol.RPC_REQUEST);
        req.setSessionId(invocation.getSessionId());

        req.setRequestId(invocation.getRequestId());
        String className = invocation.getTarget().getCanonicalName();
        String methodName = invocation.getMethod().getName();
        req.setService(className + "." + methodName + "()");

        req.setParams(Arrays.asList(invocation.getArgs()));

        out.add(req);
        // TODO 统计调用信息
    }

    /**
     * 发送退出会话报文
     *
     * @param msg
     * @param out
     */
    private void exitSession(OutboundMsg msg, List<Object> out) {

    }

    /**
     * 编码心跳消息
     *
     * @param msg
     * @param out
     */
    private void heartBeats(OutboundMsg msg, List<Object> out) {

    }


}
