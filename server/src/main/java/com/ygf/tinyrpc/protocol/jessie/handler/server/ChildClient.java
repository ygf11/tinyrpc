package com.ygf.tinyrpc.protocol.jessie.handler.server;

import com.ygf.tinyrpc.common.IdGenertor;
import com.ygf.tinyrpc.protocol.jessie.common.ClientInfo;
import com.ygf.tinyrpc.protocol.jessie.common.SessionManager;
import com.ygf.tinyrpc.protocol.jessie.message.Header;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;
import static com.ygf.tinyrpc.protocol.jessie.common.SessionStatus.*;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * rpc协议的处理逻辑，接收报文：
 * 1. CREATE_SESSION_REQUEST
 * 2. CREATE_SESSION_ACK
 * 3. RPC_REQUEST
 * 4. EXIT_SESSION
 * 5. HEARTBEAT
 * <p>
 * 可以发出的报文：
 * 1. RPC_RESPONSE
 *
 * @author theo
 * @date 20181202
 */
public class ChildClient {

    private Channel channel;

    private static Logger logger = LoggerFactory.getLogger(ChildClient.class);

    //TODO 采用所有连接共用一个ChildHandler的模式

    /**
     * 根据报文中的type 处理具体请求
     *
     * @param msg
     */
    public void parseType(Header msg) {
        if (msg == null) {
            logger.info("msg is null");
            return;
        }

        switch (msg.getType()) {
            case CREATE_SESSION_REQUEST:
                createSession(msg);
                break;
            case RPC_REQUEST:
                break;
            case EXIT_SESSION:
                break;
            default:
                logger.error("msg type {] not support");

        }

    }

    /**
     * 响应客户端的会话创建请求
     *
     * @param msg
     */
    public void createSession(Header msg) {
        String addr = getClientAddr();
        ClientInfo info = SessionManager.getSession(addr);
        // 之前已经有会话连接
        if (info == null) {
            info = new ClientInfo();
            info.setSessionId(IdGenertor.incrementAndGet());
        }

        info.setChannel(channel);
        info.setAddr(addr);
        info.setStatus(CONNECTING);

        Header response = new Header(msg);
        response.setSessionId(info.getSessionId());

    }

    /**
     * 创建会话成功
     */
    public void createSuccess() {

    }

    /**
     * rpc请求
     */
    public void rpcRequest() {

    }

    /**
     * 发出rpc响应
     */
    public void rpcResponse() {

    }

    /**
     * 销毁连接会话
     */
    public void exitSession() {

    }

    /**
     * 获取客户端到地址 ip:port
     *
     * @return
     */
    private String getClientAddr() {
        InetSocketAddress addr = (InetSocketAddress) channel.remoteAddress();
        String ip = addr.getAddress().getHostAddress();
        int port = addr.getPort();

        return ip + ":" + port;
    }
}
