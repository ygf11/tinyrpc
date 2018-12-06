package com.ygf.tinyrpc.protocol.jessie.handler.client;


import com.ygf.tinyrpc.common.RpcInvocation;
import com.ygf.tinyrpc.protocol.jessie.common.Session;
import com.ygf.tinyrpc.protocol.jessie.message.Header;
import com.ygf.tinyrpc.protocol.jessie.message.RpcRequestMessage;
import com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol;
import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;
import com.ygf.tinyrpc.protocol.jessie.message.RpcResponseMessage;
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
public class RpcHandler {

    private static final Logger logger = LoggerFactory.getLogger(RpcHandler.class);
    /**
     * 与服务器通信的channel
     */
    private Channel channel;

    public RpcHandler(Channel channel) {
        this.channel = channel;
    }

    /**
     * 发送创建会话请求
     */
    public void createSession() {
        if (channel == null) {
            logger.warn("connection is not completed");
        }


        final Header header = new Header();
        header.setProtocol(PROTOCOL);
        header.setVersion(CURRENT_VERSION);
        header.setType(CREATE_SESSION_REQUEST);
        header.setSessionId(0);

        writeMsg(header);

        Session session = Session.getInstance();
        session.setStatus(CONNECTING);
    }

    /**
     * 发送rpc请求
     *
     * @param invocation
     */
    public void RpcRequest(RpcInvocation invocation) {
        if (channel == null) {
            logger.warn("connection is not completed");
        }

        Session session = Session.getInstance();
        RpcRequestMessage msg = new RpcRequestMessage();
        msg.setProtocol(JessieProtocol.PROTOCOL);
        msg.setVersion(JessieProtocol.CURRENT_VERSION);
        msg.setType(JessieProtocol.RPC_REQUEST);
        msg.setSessionId(session.getSessionId());

        msg.setRequestId(invocation.getRequestId());
        String className = invocation.getTarget().getCanonicalName();
        String methodName = invocation.getMethod().getName();
        msg.setService(className+"."+methodName+"()");
        msg.setParams(Arrays.asList(invocation.getArgs()));

        writeMsg(msg);

        // TODO 统计调用信息
    }

    /**
     * 发送退出会话的请求
     */
    public void exit() {

    }

    /**
     * 接收服务器的响应
     */
    public void handleResponse(Header msg) {
        if (msg == null){
            logger.warn("msg is null in rpc handler");
            return;
        }
        switch (msg.getType()){
            case CREATE_SESSION_RESPONSE:
                sessionResponse(msg);
                break;
            case RPC_RESPONSE:
                rpcResponse(msg);
                break;
        }
    }

    /**
     * 处理来自服务器创建会话的响应
     */
    public void sessionResponse(Header msg) {
        final Header ack = new Header();
        ack.setProtocol(PROTOCOL);
        ack.setSessionId(msg.getSessionId());
        ack.setType(CREATE_SESSION_ACK);
        ack.setVersion(CURRENT_VERSION);

        writeMsg(ack);


        // TODO 启动心跳线程
        assert msg.getSessionId() != 0;
        Session session = Session.getInstance();
        session.setSessionId(msg.getSessionId());
        session.setStatus(CONNECTED);
    }

    /**
     * 处理来自服务器的rpc响应
     */
    public void rpcResponse(Header header) {
        boolean isResponse = header instanceof RpcResponseMessage;
        if (!isResponse){
            logger.warn("msg {] is not a response msg", header);
            return;
        }

        RpcResponseMessage msg = (RpcResponseMessage) header;
        int requestId = msg.getRequestId();

    }

    /**
     * 两种方式向channel写入消息：
     * 1. 当前线程在channel注册的eventloop中时，直接在这个线程中执行代码
     * 2. 当前线程不在channel注册的evetloop时， 提交到这个线程中稍候执行
     *
     * @param msg
     */
    private void writeMsg(final Header msg){
        if (channel.eventLoop().inEventLoop()){
            channel.write(msg);
        }else{
            channel.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    channel.write(msg);
                }
            });
        }
    }

}
