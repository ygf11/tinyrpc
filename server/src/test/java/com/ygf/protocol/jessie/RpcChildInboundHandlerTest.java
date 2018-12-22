package com.ygf.protocol.jessie;

import com.ygf.tinyrpc.protocol.jessie.message.RpcRequestMessage;
import com.ygf.tinyrpc.rpc.server.RpcChildServer;
import org.junit.Before;
import org.junit.Test;
import io.netty.channel.Channel;

import static org.mockito.Mockito.*;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import com.ygf.tinyrpc.protocol.jessie.message.InitSessionMessage;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

import com.ygf.tinyrpc.protocol.jessie.handler.server.RpcChildInboundHandler;

/**
 * 服务器端rpcChildHandler入站的测试代码
 *
 * @author theo
 * @date 20181219
 */
public class RpcChildInboundHandlerTest {
    private RpcChildInboundHandler handler;
    private Channel channel;
    private RpcChildServer server;
    private InetSocketAddress address;
    private ChannelHandlerContext ctx;
    private String service = "com.ygf.Service";
    private String appName = "spring-cloud";
    private String method = "test";

    @Before
    public void setup() {
        server = mock(RpcChildServer.class);
        handler = new RpcChildInboundHandler(server);
        channel = mock(Channel.class);
        address = new InetSocketAddress("192.168.0.1", 101);
        ctx = mock(ChannelHandlerContext.class);
    }

    /**
     * 接收会话请求
     */
    @Test
    public void sessionRequest() throws Exception{
        when(ctx.channel()).thenReturn(channel);
        when(channel.remoteAddress()).thenReturn(address);

        InitSessionMessage msg = new InitSessionMessage();
        msg.setProtocol(PROTOCOL);
        msg.setVersion(CURRENT_VERSION);
        msg.setType(CREATE_SESSION_REQUEST);
        msg.setSessionId(0);
        msg.setService(service);
        msg.setAppName(appName);

        handler.channelRead(ctx, msg);
    }

    /**
     * 接收rpc请求
     */
    @Test
    public void rpcRequest() throws Exception{
        when(ctx.channel()).thenReturn(channel);
        when(channel.remoteAddress()).thenReturn(address);

        RpcRequestMessage msg = new RpcRequestMessage();
        msg.setProtocol(PROTOCOL);
        msg.setVersion(CURRENT_VERSION);
        msg.setType(RPC_REQUEST);
        msg.setSessionId(123);
        msg.setRequestId(110);
        msg.setMethod(service+":"+method);
        // 参数类型
        List<String> paramTypes = new ArrayList<String>();
        paramTypes.add("java.lang.Integer");
        paramTypes.add("java.lang.String");
        paramTypes.add("java.lang.Integer");
        msg.setParamTypes(paramTypes);
        // 参数值
        List<Object> params = new ArrayList<Object>();
        params.add(1);
        params.add("2");
        params.add(3);
        msg.setParamTypes(paramTypes);
        handler.channelRead(ctx, msg);
    }
}
