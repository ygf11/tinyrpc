package ygf.tinyrpc.rpc.server;

import ygf.tinyrpc.common.IdGenerator;
import ygf.tinyrpc.common.RpcMetaData;
import ygf.tinyrpc.common.RpcResult;
import ygf.tinyrpc.protocol.jessie.common.ServerSession;
import ygf.tinyrpc.rpc.AbstractWriter;
import ygf.tinyrpc.rpc.Exception.RpcException;
import ygf.tinyrpc.rpc.OutboundMsg;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import static ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;
import static ygf.tinyrpc.protocol.jessie.common.SessionStatus.*;

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
     * 当前暴露的服务
     */
    private Map<String, Object> exportedServices = new ConcurrentHashMap<>();
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
        logger.info("addr: {}, appName: {}, service: {}", addr, appName);
        ServerSession session = sessionMap.get(addr);

        session.setAppName(appName);

        Class cz = findClass(service);
        session.setService(cz);

        session.setStatus(CONNECTED);

        logger.info("session: {}", session);
        // 发送session响应消息
        responseSessionInit(session);
    }

    /**
     * 发送会话响应消息
     *
     * @param session
     */
    private void responseSessionInit(ServerSession session) {
        OutboundMsg msg = new OutboundMsg();
        msg.setType(CREATE_SESSION_RESPONSE);
        msg.setArg(session.getSessionId());

        writeMsg(session, msg);
    }

    /**
     * 处理rpc请求
     * // TODO rpcMetaData只需要传个方法名就好了
     * // TODO 当调用出现异常时抛出rpcException
     *
     * @param addr
     * @param metaData
     */
    public void handleRpcRequest(String addr, RpcMetaData metaData) throws Exception {
        ServerSession session = sessionMap.get(addr);
        Class cz = findClass(metaData.getService());
        Class[] paramTypes = findClasses(metaData.getParamTypes());
        Method method = cz.getMethod(metaData.getMethod(), paramTypes);

        // spring上下文中获取目标服务对象
        String iName = session.getService().getCanonicalName();
        Object service = exportedServices.get(session.getService().getCanonicalName());

        if (service == null) {
            logger.error("no exported {} service", iName);
            return;
        }

        // 调用目标方法
        Class type = null;
        Object result = null;
        try {
            Object[] args = metaData.getArgs().toArray();
            result = method.invoke(service, args);
        } catch (Exception e) {
            logger.error("exception when invoke method:{}", e);
            type = RpcException.class;
        }

        RpcResult rpcResult = new RpcResult();
        // sessionId
        rpcResult.setSessionId(session.getSessionId());
        // requestId
        rpcResult.setRequestId(metaData.getRequestId());
        Class returnType = type == null ? method.getReturnType() : type;
        String resultType = returnType.getCanonicalName();
        // type
        rpcResult.setResultType(resultType);
        // obj
        rpcResult.setResult(result);

        logger.info("result:{}", rpcResult);
        responseRpcRequest(session, rpcResult);
    }

    /**
     * 将rpc调用结果写入channel
     *
     * @param session
     * @param result
     */
    private void responseRpcRequest(ServerSession session, RpcResult result) {
        OutboundMsg msg = new OutboundMsg();
        msg.setType(RPC_RESPONSE);
        msg.setArg(result);
        writeMsg(session, msg);

    }

    /**
     * 获取一个class的列表
     *
     * @param list
     * @return
     */
    private Class[] findClasses(List<String> list) throws ClassNotFoundException {
        Class[] result = new Class[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            result[i] = findClass(list.get(i));
        }

        return result;
    }


    /**
     * 根据类名寻找服务类
     *
     * @param service
     * @return
     */
    private Class findClass(String service) throws ClassNotFoundException {
        Class cz = null;
        Thread current = Thread.currentThread();
        ClassLoader loader = current.getContextClassLoader();
        cz = Class.forName(service, false, loader);

        return cz;

    }

    /**
     * 添加已经暴露的服务
     *
     * @param service
     * @param ref
     */
    public void addExported(String service, Object ref) {
        exportedServices.put(service, ref);
    }

    /**
     * 向server注册channel
     *
     * @param addr
     * @param channel
     */
    public void registerAsyncChannel(String addr, Channel channel) {
        synchronized (channel) {
            ServerSession session = sessionMap.get(addr);
            if (session != null) {
                throw new RuntimeException("remote connection exists for " + addr);
            }

            session = new ServerSession();
            session.setSessionId(SESSIONID.get());
            sessionMap.put(addr, session);
            registerChannel(session, channel);
        }
    }
}
