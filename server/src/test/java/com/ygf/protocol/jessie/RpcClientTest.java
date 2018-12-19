package com.ygf.protocol.jessie;

import com.ygf.protocol.jessie.api.Service;
import com.ygf.tinyrpc.common.RpcResult;
import com.ygf.tinyrpc.protocol.jessie.common.Session;
import com.ygf.tinyrpc.rpc.OutboundMsg;
import com.ygf.tinyrpc.rpc.client.RpcClient;
import io.netty.channel.Channel;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import org.apache.commons.lang3.reflect.FieldUtils;

import static com.ygf.tinyrpc.protocol.jessie.common.SessionStatus.*;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * rpcClient类的测试代码
 *
 * @author theo
 * @date 20181211
 */
public class RpcClientTest {
    private static Logger logger = LoggerFactory.getLogger(RpcClientTest.class);
    RpcClient rpcClient;
    Channel channel;
    final Session session = new Session();

    @Before
    public void setup() {
        rpcClient = new RpcClient();
        channel = mock(Channel.class);
        session.setStatus(CONNECTING);
        session.setService(Service.class);
        rpcClient.registerChannel(session, channel);
    }

    /**
     * 初始化session的测试
     */
    @Test
    public void initSession() throws Exception {
        EventLoop eventLoop = mock(DefaultEventLoop.class);
        when(channel.eventLoop()).thenReturn(eventLoop);
        when(eventLoop.inEventLoop()).thenReturn(Boolean.TRUE);
        when(channel.write(any(OutboundMsg.class))).thenReturn(null);
        final String appName = "spring-cloud";
        new Thread(new Runnable() {
            @Override
            public void run() {
                rpcClient.initSession(session, appName);
                logger.info("thread continue!!");
            }
        }).start();

        Thread.sleep(5000);
        Object[] args = new Object[1];
        args[0] = session;
        Class[] types = new Class[1];
        types[0] = Session.class;
        MethodUtils.invokeMethod(rpcClient, true, "notify", args, types);

        Thread.sleep(5000);
    }

    /**
     * 处理创建会话的响应代码
     */
    @Test
    public void handleSessionInit() throws Exception {
        String addr = "192.168.0.1:901";
        Field field = FieldUtils.getField(RpcClient.class, "sessionMap", true);
        Map<String, Session> sessionMap = (Map<String, Session>) field.get(rpcClient);
        sessionMap.put(addr, session);

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (session) {
                    logger.info("thread start");
                    try {
                        session.wait();
                        logger.info("thread continue");
                    } catch (InterruptedException e) {

                    }
                }
            }
        }).start();

        Thread.sleep(2000);

        rpcClient.handleSessionInit(addr, 123);

        Thread.sleep(2000);
    }

    /**
     * rpc请求测试代码
     *
     * @throws Exception
     */
    @Test
    public void rpcRequest() throws Exception {
        String addr = "192.168.0.1:901";
        EventLoop eventLoop = mock(DefaultEventLoop.class);
        when(channel.eventLoop()).thenReturn(eventLoop);
        when(eventLoop.inEventLoop()).thenReturn(Boolean.TRUE);
        when(channel.write(any(OutboundMsg.class))).thenReturn(null);
        final Class service = Service.class;
        final Method method = Service.class.getMethod("test", int.class, String.class);
        final Object[] params = new Object[2];
        params[0] = 1;
        params[1] = "a";


        Field field = FieldUtils.getField(RpcClient.class, "sessionMap", true);
        Map<String, Session> sessionMap = (Map<String, Session>) field.get(rpcClient);
        session.setStatus(CONNECTED);
        session.setService(Service.class);
        session.setSessionId(110);
        sessionMap.put(addr, session);

        new Thread(new Runnable() {
            @Override
            public void run() {
                rpcClient.rpcRequest(session, method, params);
                logger.info("thread continue!");
            }
        }).start();

        Thread.sleep(1000);
        //wake up thread
        field = FieldUtils.getField(RpcClient.class, "rpcWaiters", true);
        Map<Integer, Object> map = (Map<Integer, Object>) field.get(rpcClient);
        Object sync = map.get(1);
        synchronized (sync) {
            sync.notify();
            logger.info("finish notify!!");
        }

        Thread.sleep(10000);
    }

    /**
     * rpc响应测试代码
     *
     * @throws Exception
     */
    @Test
    public void rpcResponse() throws Exception {
        String addr = "192.168.0.1:901";
        Integer requestId = 123;
        RpcResult result = new RpcResult();
        result.setResult(100);
        result.setResultType(Integer.class.getCanonicalName());

        Field field = FieldUtils.getField(RpcClient.class, "sessionMap", true);
        Map<String, Session> sessionMap = (Map<String, Session>) field.get(rpcClient);
        session.setStatus(CONNECTED);
        session.setService(Service.class);
        session.setSessionId(110);
        sessionMap.put(addr, session);

        field = FieldUtils.getField(RpcClient.class, "rpcWaiters", true);
        Map<Integer, Object> syncs = (Map<Integer, Object>) field.get(rpcClient);
        final Object sync = new Object();
        syncs.put(123, sync);
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (sync) {
                    try {
                        sync.wait();
                    } catch (Exception e) {

                    }
                    logger.info("thread continue!");
                }
            }
        }).start();

        Thread.sleep(2000);
        rpcClient.handleRpcResponse(addr, requestId, result);
        Thread.sleep(2000);
    }
}
