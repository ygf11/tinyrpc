package com.ygf.tinyrpc.protocol.jessie.handler.client;

import com.ygf.tinyrpc.common.RpcMetaData;
import com.ygf.tinyrpc.common.InitParams;
import com.ygf.tinyrpc.protocol.jessie.message.InitSessionMessage;
import com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol;
import com.ygf.tinyrpc.protocol.jessie.message.RpcRequestMessage;
import com.ygf.tinyrpc.rpc.OutboundMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;
import java.util.List;

/**
 * 将出站请求转化为编码器可以编码的请求对象
 * TODO
 *
 * @author theo
 * @date 20181208
 */
public class RpcOutboundHandler extends MessageToMessageEncoder<OutboundMsg> {

    private static final Logger logger = LoggerFactory.getLogger(RpcOutboundHandler.class);

    public RpcOutboundHandler() {
        super();
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
        boolean isInit = arg instanceof InitParams;
        if (!isInit) {
            logger.error("type {} not matched arg {}", msg.getType(), arg);
            return;
        }

        InitParams params = (InitParams) arg;

        final InitSessionMessage req = new InitSessionMessage();
        req.setProtocol(PROTOCOL);
        req.setVersion(CURRENT_VERSION);
        req.setType(CREATE_SESSION_REQUEST);
        //req.setSessionId(0);
        req.setAppName(params.getAppName());
        req.setService(params.getService());

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
        boolean isInvoker = arg instanceof RpcMetaData;
        if (!isInvoker) {
            logger.error("type {} not matched arg {}", msg.getType(), arg);
            return;
        }
        // rpc请求参数
        RpcMetaData metaData = (RpcMetaData) arg;
        RpcRequestMessage req = new RpcRequestMessage();
        req.setProtocol(JessieProtocol.PROTOCOL);
        req.setVersion(JessieProtocol.CURRENT_VERSION);
        req.setType(JessieProtocol.RPC_REQUEST);
        req.setSessionId(metaData.getSessionId());

        req.setRequestId(metaData.getRequestId());
        req.setMethod(getServiceName(metaData));
        req.setParamTypes(metaData.getParamTypes());
        req.setParams(metaData.getArgs());;

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

    /**
     * 组成服务名(类名+":"+方法名)
     *
     * @param rpcMetaData
     * @return
     */
    private String getServiceName(RpcMetaData rpcMetaData) {
        String className = rpcMetaData.getService();
        String methodName = rpcMetaData.getMethod();
        return className + ":" + methodName;
    }

}
