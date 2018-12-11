package com.ygf.protocol.jessie;

import com.ygf.tinyrpc.common.RpcResult;
import com.ygf.tinyrpc.protocol.jessie.common.Session;
import com.ygf.tinyrpc.protocol.jessie.handler.client.RpcClient;
import com.ygf.tinyrpc.protocol.jessie.message.Header;
import com.ygf.tinyrpc.protocol.jessie.message.RpcResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;
import static com.ygf.tinyrpc.protocol.jessie.common.SessionStatus.*;
import static com.ygf.tinyrpc.common.RpcResponseType.*;

/**
 * RpcHandler这一层的测试代码，包括：
 * 1. 发送创建会话请求
 * 2. 接受创建会话相应
 * 3. 发起rpc请求
 * 4. 接收rpc响应
 *
 */
public class RpcHandlerTest{

    private RpcClient handler;
    private EventLoop eventLoop;
    private Channel channel;

    @Before
    public void setup(){
        channel = Mockito.mock(Channel.class);
        eventLoop = Mockito.mock(DefaultEventLoop.class);
        handler = new RpcClient(channel);
    }

    /**
     * 响应会话创建
     */
    @Test
    public void sessionResponse(){
        when(channel.eventLoop()).thenReturn(eventLoop);
        when(eventLoop.inEventLoop()).thenReturn(true);
        when(channel.write(any(Object.class))).thenReturn(null);
        Header header = new Header();
        header.setProtocol(PROTOCOL);
        header.setVersion(CURRENT_VERSION);
        header.setType(CREATE_SESSION_RESPONSE);
        header.setSessionId(123);

        handler.handleResponse(header);

        Assert.assertEquals((long)123, (long)Session.getInstance().getSessionId());
        Assert.assertEquals(CONNECTED, Session.getInstance().getStatus());
    }

    /**
     * rpc请求响应
     */
    @Test
    public void RpcResponse(){
        RpcResponseMessage msg = new RpcResponseMessage();
        msg.setProtocol(PROTOCOL);
        msg.setVersion(CURRENT_VERSION);
        msg.setType(RPC_RESPONSE);
        msg.setSessionId(123);
        msg.setRequestId(1);
        msg.setResultType(NORMAL);
        msg.setService("com.ygf.Test.test()");
        msg.setTargetClass("java.lang.Integer");
        msg.setResult(1);

        handler.handleResponse(msg);

        RpcResult result = Session.getInstance().getResult(1);
        Assert.assertEquals(NORMAL, result.getType());
        Assert.assertEquals(1, result.getResult());
        Assert.assertEquals("java.lang.Integer", result.getResultType());
    }

    /**
     *  响应类型不是创建会话响应和rpc响应
     */
    @Test
    public void unmatchedType(){
        Header header = new Header();
        header.setProtocol(PROTOCOL);
        header.setVersion(CURRENT_VERSION);
        header.setType((byte)10);
        header.setSessionId(0);
        handler.handleResponse(header);
    }

}
