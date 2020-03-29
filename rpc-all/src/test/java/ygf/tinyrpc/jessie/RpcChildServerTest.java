package ygf.tinyrpc.jessie;

import ygf.tinyrpc.jessie.api.Service;
import ygf.tinyrpc.jessie.service.ServiceImpl;
import ygf.tinyrpc.common.RpcMetaData;
import ygf.tinyrpc.protocol.jessie.common.ServerSession;
import ygf.tinyrpc.rpc.OutboundMsg;
import ygf.tinyrpc.rpc.server.RpcChildServer;
import io.netty.channel.Channel;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static ygf.tinyrpc.protocol.jessie.common.SessionStatus.*;

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
    private ApplicationContext applicationContext;
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
    public void rpcRequestTest() throws Exception {
        EventLoop eventLoop = mock(DefaultEventLoop.class);
        when(channel.eventLoop()).thenReturn(eventLoop);
        when(eventLoop.inEventLoop()).thenReturn(true);
        when(channel.write(any(OutboundMsg.class))).thenReturn(null);

        // 添加session
        Field field = FieldUtils.getField(RpcChildServer.class, "sessionMap", true);
        Map<String, ServerSession> sessionMap = (Map<String, ServerSession>) field.get(server);
        session.setStatus(CONNECTED);
        session.setAppName("rpc-test");
        session.setService(Service.class);
        sessionMap.put(addr, session);

        // 设置applicationContext
        field = FieldUtils.getField(RpcChildServer.class, "applicationContext", true);
        applicationContext = mock(ApplicationContext.class);
        field.set(server, applicationContext);

        when(applicationContext.getBean(Service.class)).thenReturn(new ServiceImpl());

        RpcMetaData metaData = new RpcMetaData();
        metaData.setSessionId(123);
        metaData.setRequestId(110);
        metaData.setService(Service.class.getCanonicalName());
        metaData.setMethod("test");
        // 参数类型
        List<String> paramTypes = new ArrayList<String>();
        paramTypes.add("java.lang.Integer");
        paramTypes.add("java.lang.String");
        metaData.setParamTypes(paramTypes);
        // 参数值
        List<Object> params = new ArrayList<Object>();
        params.add(100);
        params.add("rpc");
        metaData.setArgs(params);

        server.handleRpcRequest(addr, metaData);
    }


}
