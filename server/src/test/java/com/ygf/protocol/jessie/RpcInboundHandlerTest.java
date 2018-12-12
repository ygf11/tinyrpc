package com.ygf.protocol.jessie;

import com.ygf.protocol.jessie.api.Service;
import com.ygf.tinyrpc.protocol.jessie.message.Header;
import com.ygf.tinyrpc.protocol.jessie.message.RpcResponseMessage;
import com.ygf.tinyrpc.rpc.client.RpcClient;
import com.ygf.tinyrpc.protocol.jessie.handler.client.RpcInboundHandler;
import io.netty.channel.ChannelHandlerContext;
import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;
import static com.ygf.tinyrpc.common.RpcResponseType.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * rpcInboundHandler类的测试
 *
 * @author theo
 * @date 20181211
 */
public class RpcInboundHandlerTest {

    private RpcInboundHandler handler;
    private Class service;
    private RpcClient rpcClient;

    @Before
    public void setup(){
        service = Service.class;
        RpcClient rpcClient = Mockito.mock(RpcClient.class);
        handler = new RpcInboundHandler(service, rpcClient);
    }

    /**
     * 测试会话响应
     */
    @Test
    public void sessionResponse() throws Exception{
        ChannelHandlerContext ctx = null;
        Header msg = new Header();
        msg.setProtocol(PROTOCOL);
        msg.setVersion(CURRENT_VERSION);
        msg.setType(CREATE_SESSION_RESPONSE);
        msg.setSessionId(123);
        handler.channelRead(ctx, msg);
    }

    /**
     * 测试rpc响应
     *
     * @throws Exception
     */
    @Test
    public void rpcResponse() throws Exception{
        ChannelHandlerContext ctx = null;
        RpcResponseMessage msg = new RpcResponseMessage();
        msg.setProtocol(PROTOCOL);
        msg.setVersion(CURRENT_VERSION);
        msg.setType(RPC_RESPONSE);
        msg.setSessionId(123);
        msg.setRequestId(50);
        msg.setTargetClass(Integer.class.getCanonicalName());
        msg.setService(service.getCanonicalName());
        msg.setResultType(NORMAL);
        msg.setResult(100);
        handler.channelRead(ctx, msg);
    }

}