package ygf.tinyrpc.jessie;

import ygf.tinyrpc.jessie.api.Service;
import ygf.tinyrpc.protocol.jessie.message.Header;
import ygf.tinyrpc.protocol.jessie.message.RpcResponseMessage;
import ygf.tinyrpc.rpc.client.RpcClient;
import ygf.tinyrpc.protocol.jessie.handler.client.RpcInboundHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import static ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.net.InetSocketAddress;

/**
 * rpcInboundHandler类的测试
 *
 * @author theo
 * @date 20181211
 */
public class RpcInboundHandlerTest {

    private RpcInboundHandler handler;
    private Class service;
    private InetSocketAddress address;
    private Channel channel;
    private ChannelHandlerContext ctx;
    @Before
    public void setup(){
        service = Service.class;
        RpcClient rpcClient = mock(RpcClient.class);
        address = new InetSocketAddress("192.168.72.1", 902);
        handler = new RpcInboundHandler(rpcClient);
        channel = mock(Channel.class);
        ctx = mock(ChannelHandlerContext.class);
    }

    /**
     * 测试会话响应
     */
    @Test
    public void sessionResponse() throws Exception{
        when(ctx.channel()).thenReturn(channel);
        when(channel.remoteAddress()).thenReturn(address);

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
        when(ctx.channel()).thenReturn(channel);
        when(channel.remoteAddress()).thenReturn(address);
        RpcResponseMessage msg = new RpcResponseMessage();
        msg.setProtocol(PROTOCOL);
        msg.setVersion(CURRENT_VERSION);
        msg.setType(RPC_RESPONSE);
        msg.setSessionId(123);
        msg.setRequestId(50);
        msg.setResultClass(Integer.class.getCanonicalName());
        msg.setService(service.getCanonicalName());
        msg.setResult(100);
        handler.channelRead(ctx, msg);
    }

}
