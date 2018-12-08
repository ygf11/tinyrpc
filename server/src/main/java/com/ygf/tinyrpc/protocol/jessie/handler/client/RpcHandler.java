package com.ygf.tinyrpc.protocol.jessie.handler.client;


import com.ygf.tinyrpc.common.RpcInvocation;
import com.ygf.tinyrpc.common.RpcResult;
import com.ygf.tinyrpc.protocol.jessie.common.Session;
import com.ygf.tinyrpc.protocol.jessie.handler.AbstractHandler;
import com.ygf.tinyrpc.protocol.jessie.message.*;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

import static com.ygf.tinyrpc.protocol.jessie.common.SessionStatus.*;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * rpc处理逻辑
 *
 * @author theo
 * @date 20181202
 */
public class RpcHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(RpcHandler.class);
    /**
     * 单例会话对象
     */
    private Session session = Session.getInstance();

    public RpcHandler(Channel channel) {
        super(channel);
    }

    /**
     * 发送退出会话的请求
     */
    public void exit() {

    }

    /**
     * 接收服务器的响应
     *
     * @param msg
     */
    public void handleResponse(Header msg) {
        if (msg == null) {
            logger.warn("msg is null in rpc handler");
            return;
        }

        switch (msg.getType()) {
            case CREATE_SESSION_RESPONSE:
                sessionResponse(msg);
                break;
            case RPC_RESPONSE:
                rpcResponse(msg);
                break;
            default:
                logger.warn("msg type {}, not support", msg.getType());
        }
    }

    /**
     * 处理来自服务器创建会话的响应
     *
     * @param msg
     */
    public void sessionResponse(Header msg) {
        // TODO 启动心跳线程
        assert msg.getSessionId() != 0;
        session.setSessionId(msg.getSessionId());
        session.setStatus(CONNECTED);
    }

    /**
     * 处理来自服务器的rpc响应
     *
     * @param header
     */
    public void rpcResponse(Header header) {
        boolean isResponse = header instanceof RpcResponseMessage;
        if (!isResponse) {
            logger.warn("msg {] is not a response msg", header);
            return;
        }

        RpcResponseMessage msg = (RpcResponseMessage) header;
        int requestId = msg.getRequestId();
        RpcResult result = new RpcResult();
        result.setType(msg.getResultType());
        result.setResultType(msg.getTargetClass());
        result.setResult(msg.getResult());

        session.putResult(requestId, result);

        // TODO 更新心跳

    }
}
