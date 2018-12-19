package com.ygf.protocol.jessie;

import com.ygf.tinyrpc.common.RpcResult;
import com.ygf.tinyrpc.protocol.jessie.handler.server.RpcChildOutboundHandler;
import com.ygf.tinyrpc.protocol.jessie.message.Header;
import com.ygf.tinyrpc.protocol.jessie.message.RpcResponseMessage;
import com.ygf.tinyrpc.rpc.OutboundMsg;
import com.ygf.tinyrpc.rpc.server.RpcChildServer;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

/**
 * 服务器端outbound 处理器测试代码
 *
 * @author theo
 * @date 20181219
 */
public class RpcChildOutboundHandlerTest {
    private RpcChildOutboundHandler handler;
    private RpcChildServer server;
    private List<Object> out;
    private Class[] types;
    private Object[] args;

    @Before
    public void setup() {
        server = mock(RpcChildServer.class);
        handler = new RpcChildOutboundHandler();
        out = new ArrayList<>();
        types = new Class[3];
        types[0] = ChannelHandlerContext.class;
        types[1] = OutboundMsg.class;
        types[2] = List.class;

        args = new Object[3];
        args[0] = null;
        args[1] = null;
        args[2] = out;
    }

    /**
     * 创建会话响应代码
     */
    @Test
    public void sessionInitResponse() throws Exception{
        OutboundMsg msg = new OutboundMsg();
        int sessionId = 123;
        msg.setType(CREATE_SESSION_RESPONSE);
        msg.setArg(sessionId);
        args[1] = msg;
        MethodUtils.invokeMethod(handler, true, "encode", args, types);

        Header header = (Header)out.get(0);
        Assert.assertEquals(PROTOCOL, header.getProtocol());
        Assert.assertEquals(CREATE_SESSION_RESPONSE, header.getType());
        Assert.assertEquals(sessionId, header.getSessionId());
    }

    /**
     * 响应rpc请求
     */
    @Test
    public void rpcResponse() throws Exception{
        OutboundMsg msg = new OutboundMsg();
        msg.setType(RPC_RESPONSE);
        RpcResult result = new RpcResult();
        result.setSessionId(123);
        result.setRequestId(110);
        result.setResultType("java.lang.Integer");
        result.setResult(1);
        msg.setArg(result);

        args[1] = msg;

        MethodUtils.invokeMethod(handler, true, "encode", args, types);

        RpcResponseMessage header = (RpcResponseMessage)out.get(0);
        Assert.assertEquals(PROTOCOL, header.getProtocol());
        Assert.assertEquals(RPC_RESPONSE, header.getType());
        Assert.assertEquals(123, header.getSessionId());
        Assert.assertEquals(110, (int)header.getRequestId());
        Assert.assertEquals("java.lang.Integer", header.getResultClass());
        Assert.assertEquals(1, header.getResult());
    }
}
