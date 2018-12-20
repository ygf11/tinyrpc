package com.ygf.protocol.jessie;

import com.ygf.tinyrpc.protocol.jessie.common.ServerSession;
import com.ygf.tinyrpc.rpc.OutboundMsg;
import com.ygf.tinyrpc.rpc.server.RpcChildServer;
import io.netty.channel.Channel;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

import static org.mockito.Mockito.*;
import static com.ygf.tinyrpc.protocol.jessie.common.SessionStatus.*;

/**
 * rpc child server的单元测试
 *
 * @author theo
 * @date 20181218
 */
public class RpcChildServerTest {
    private static Logger logger = LoggerFactory.getLogger(RpcChildServerTest.class);

    private RpcChildServer server;
    private Channel channel;
    private ServerSession session;
    private String addr = "192.168.0.1:901";
    private String appName = "spring-cloud";
    private String service = "java.lang.Integer";


    @Before
    public void setup() {
        session = new ServerSession();
        session.setStatus(CONNECTING);
        server = new RpcChildServer();
        channel = mock(Channel.class);
        server.registerChannel(session, channel);
    }

    /**
     * 处理session请求
     *
     * @throws Exception
     */
    @Test
    public void sessionInitTest() throws Exception {
        EventLoop eventLoop = mock(DefaultEventLoop.class);
        when(channel.eventLoop()).thenReturn(eventLoop);
        when(eventLoop.inEventLoop()).thenReturn(true);
        when(channel.write(any(OutboundMsg.class))).thenReturn(null);

        Field field = FieldUtils.getField(RpcChildServer.class, "sessionMap", true);
        Map<String, ServerSession> sessionMap = (Map<String, ServerSession>) field.get(server);
        sessionMap.put(addr, session);

        server.handleSessionInit(addr, appName, service);
    }


    /**
     * 处理rpc请求
     */
    @Test
    public void rpcRequestTest() {

    }


}
