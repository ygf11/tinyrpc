package com.ygf.protocol.jessie;

import com.ygf.protocol.jessie.api.Service;
import com.ygf.tinyrpc.protocol.jessie.common.Session;
import com.ygf.tinyrpc.rpc.client.RpcClient;
import io.netty.channel.Channel;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import org.apache.commons.lang3.reflect.FieldUtils;

import static com.ygf.tinyrpc.protocol.jessie.common.SessionStatus.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
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

    @Before
    public void setup() {
        rpcClient = new RpcClient();
        channel = mock(Channel.class);
        rpcClient.resgiterChannel(Service.class, channel);
    }

    /**
     * 初始化session的测试
     */
    @Test
    public void initSession() throws Exception {
        EventLoop eventLoop = mock(DefaultEventLoop.class);
        when(channel.eventLoop()).thenReturn(eventLoop);
        when(eventLoop.inEventLoop()).thenReturn(Boolean.TRUE);
        String appName = "spring-cloud";
        new Thread(new Runnable() {
            @Override
            public void run() {
                rpcClient.initSession(Service.class, appName);
                logger.info("thread continue!!");
            }
        }).start();

        Field field = FieldUtils.getField(RpcClient.class, "waitObjects", true);
        logger.info("name {}", field.getName());
        logger.info("get: {}", field.get(rpcClient));
        Map<Integer, Object> map = (Map<Integer, Object>) field.get(rpcClient);
        //Object obj = map.get();
        synchronized (Service.class) {
            Service.class.notify();
            logger.info("finish notify!!");
            Thread.sleep(10000);
        }
    }

    /**
     * 处理创建会话的响应代码
     */
    @Test
    public void handleSessionInit() throws Exception {
        Field field = FieldUtils.getField(RpcClient.class, "sessionMap", true);
        Map<Class, Session> sessionMap = (Map<Class, Session>) field.get(rpcClient);
        Session session = new Session();
        session.setStatus(CONNECTING);
        sessionMap.put(Service.class, session);

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (Service.class) {
                    logger.info("thread start");
                    try {
                        Service.class.wait();
                        logger.info("thread continue");
                    } catch (InterruptedException e) {

                    }
                }
            }
        }).start();

        rpcClient.handleSessionInit(Service.class, 123);

        Thread.sleep(20000);
    }

    public void rpcRequest(){

    }
}
