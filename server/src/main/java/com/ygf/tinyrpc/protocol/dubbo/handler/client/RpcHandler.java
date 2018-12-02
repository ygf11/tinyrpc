package com.ygf.tinyrpc.protocol.dubbo.handler.client;


import com.ygf.tinyrpc.common.RpcInvocation;
import com.ygf.tinyrpc.protocol.dubbo.message.Header;
import com.ygf.tinyrpc.protocol.dubbo.message.RpcRequestMessage;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ygf.tinyrpc.protocol.dubbo.message.DubboProtocol.*;


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
        if (channel.eventLoop().inEventLoop()) {
            channel.write(header);
        } else {
            channel.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    channel.write(header);
                }
            });
        }
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

        RpcRequestMessage msg = new RpcRequestMessage();
        msg.setProtocol(PROTOCOL);
        msg.setVersion(CURRENT_VERSION);
        msg.setType(RPC_REQUEST);

    }

    /**
     * 发送退出会话的请求
     */
    public void exit() {

    }

    /**
     * 接收服务器的响应
     */
    public void handleResponse() {

    }

    /**
     * 处理来自服务器创建会话的响应
     */
    public void sessionResponse() {

    }

    /**
     * 处理来自服务器的rpc响应
     */
    public void rpcResponse() {

    }


}
