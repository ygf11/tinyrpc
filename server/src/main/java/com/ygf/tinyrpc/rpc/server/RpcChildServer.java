package com.ygf.tinyrpc.rpc.server;

import com.ygf.tinyrpc.common.IdGenerator;
import com.ygf.tinyrpc.protocol.jessie.common.ServerSession;
import com.ygf.tinyrpc.protocol.jessie.common.Session;
import com.ygf.tinyrpc.rpc.AbstractWriter;
import com.ygf.tinyrpc.rpc.OutboundMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;
import static com.ygf.tinyrpc.protocol.jessie.common.SessionStatus.*;

/**
 * 服务器端与客户端通信的对等体
 * // TODO 异常处理
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
    private Map<String, ServerSession> sessionMap = new ConcurrentHashMap<String, ServerSession>();
    /**
     * 会话id生成器
     */
    private IdGenerator SESSIONID = new IdGenerator();

    /**
     * 处理创建会话请求
     *
     * @param addr
     */
    public void handleSessionInit(String addr, String appName, String service) throws ClassNotFoundException {
        ServerSession session = sessionMap.get(addr);
        Integer sessionId = session.getSessionId();
        session.setSessionId(sessionId);

        session.setAppName(appName);

        Class cz = findClass(service);
        session.setService(findClass(service));

        session.setStatus(CONNECTED);


    }

    /**
     * 发送会话响应消息
     *
     * @param session
     */
    private  void responseSessionInit(ServerSession session){
        OutboundMsg msg = new OutboundMsg();
        msg.setType(CREATE_SESSION_RESPONSE);
        msg.setArg(session.getSessionId());

        writeMsg(session, msg);
    }


    /**
     * 根据类名寻找服务类
     *
     * @param service
     * @return
     */
    private Class findClass(String service) throws ClassNotFoundException{
        Class cz = null;
        Thread current = Thread.currentThread();
        ClassLoader loader = current.getContextClassLoader();
        cz = Class.forName(service, false, loader);

        return cz;

    }
}
