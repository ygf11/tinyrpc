package com.ygf.tinyrpc.protocol.dubbo.handler.server;

import io.netty.channel.Channel;

/**
 * rpc协议的处理逻辑，接收报文：
 * 1. CREATE_SESSION_REQUEST
 * 2. CREATE_SESSION_ACK
 * 3. RPC_REQUEST
 * 4. EXIT_SESSION
 * 5. HEARTBEAT
 *
 * 可以发出的报文：
 * 1. RPC_RESPONSE
 *
 *
 * @author theo
 * @date 20181202
 */
public class ChildHandler {
    /**
     * 与客户端通信的channel
     */
    private Channel channel;

    /**
     * 创建会话
     */
    public void createSession(){

    }

    /**
     * 创建会话成功
     */
    public void createSuccess(){

    }

    /**
     * rpc请求
     */
    public void rpcRequest(){

    }

    /**
     * 发出rpc响应
     */
    public void rpcResponse(){

    }

    /**
     * 销毁连接会话
     */
    public void exitSession(){

    }
}
