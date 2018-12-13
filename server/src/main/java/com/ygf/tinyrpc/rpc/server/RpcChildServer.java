package com.ygf.tinyrpc.rpc.server;

import com.ygf.tinyrpc.protocol.jessie.common.Session;
import com.ygf.tinyrpc.rpc.AbstractWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

/**
 * 服务器端与客户端通信的对等体
 *
 * @author theo
 * @date 20181212
 */
public class RpcChildServer extends AbstractWriter {

    private static Logger logger = LoggerFactory.getLogger(RpcChildServer.class);
    /**
     * 与io处理分离的线程池
     */
    private ThreadPoolExecutor executor;
    /**
     * 服务提供者配置
     */
    private Map<Class, Object> configMap = new ConcurrentHashMap<Class, Object>();
    /**
     * 应用级别配置
     */
    private Object appConfig;
    /**
     * spring上下文
     */
    private ApplicationContext applicationContext;
    /**
     * 保存消费者地址到对应session的映射
     */
    private Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();

    /**
     * 处理创建会话请求
     *
     * @param addr
     */
    public void handleSessionInit(String addr, String appName) {

    }
}
