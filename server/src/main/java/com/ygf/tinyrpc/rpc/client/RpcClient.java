package com.ygf.tinyrpc.rpc.client;

import com.ygf.tinyrpc.common.IdGenerator;
import com.ygf.tinyrpc.common.RpcMetaData;
import com.ygf.tinyrpc.common.RpcResult;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

import com.ygf.tinyrpc.common.InitParams;
import com.ygf.tinyrpc.protocol.jessie.common.Session;
import com.ygf.tinyrpc.rpc.AbstractWriter;
import com.ygf.tinyrpc.rpc.OutboundMsg;

import static com.ygf.tinyrpc.protocol.jessie.common.SessionStatus.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消费者端协议无关的通信类
 *
 * @author theo
 * @date 20181209
 */
public class RpcClient extends AbstractWriter {
    private static Logger logger = LoggerFactory.getLogger(RpcClient.class);
    /**
     * 具体服务到对应配置的映射
     */
    private Map<Class, Object> services = new ConcurrentHashMap<Class, Object>();
    /**
     * 保存服务提供者所在地址-->服务会话的映射
     */
    private Map<String, Map<Class, Session>> acceptSessions = new ConcurrentHashMap<String, Map<Class, Session>>();
    /**
     * 保存通信地址-->session的映射
     */
    private Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();
    /**
     * 为rpc请求同步时的监视器对象
     */
    private Map<Integer, Object> rpcWaiters = new ConcurrentHashMap<Integer, Object>();
    /**
     * 应用级别的配置
     */
    private Object config;
    /**
     * 用于生成requestId
     */
    private final IdGenerator REQUESTID = new IdGenerator();

    public RpcClient() {
    }

    /**
     * 初始化
     */
    public void init() {

    }

    /**
     * 初始化会话
     *
     * @param session
     * @param appName
     */
    public void initSession(Session session, String appName) {
        // 服务发现
        // 负载均衡
        // 建立连接(单连接时 要在rpcConnector中判断是否存在对应连接  存在则不需要建立连接)
        // 创建会话
        OutboundMsg msg = new OutboundMsg();
        msg.setType(CREATE_SESSION_REQUEST);
        String service = getServiceName(session);
        msg.setArg(new InitParams(service, appName));
        logger.info("outboundMsg {}", msg);
        writeMsg(session, msg);

        // waiting
        waitForServer(session);
    }

    /**
     * 处理来自服务器端会话响应
     *
     * @param addr
     * @param sessionId
     */
    public void handleSessionInit(String addr, Integer sessionId) {
        Session session = sessionMap.get(addr);
        session.setStatus(CONNECTED);
        session.setSessionId(sessionId);

        logger.info("sessionId: {}", sessionId);
        // 唤醒等待线程
        notify(session);
        // TODO 开始心跳
    }


    public void handleSessionExit() {

    }

    /**
     * 发起rpc请求
     *
     * @param session
     * @param method
     * @param args
     */
    public void rpcRequest(Integer requestId, Session session, Method method, Object[] args) {
        // 创建rpcInvocation
        // 写入channel
        // 以一个对象作为监视器进行等待
        RpcMetaData metaData = new RpcMetaData();
        // 获取一个新的requestId(TODO由context边生成)
        // Integer requestId = REQUESTID.get();
        metaData.setSessionId(session.getSessionId());
        metaData.setRequestId(requestId);
        metaData.setService(session.getService().getCanonicalName());
        metaData.setMethod(method.getName());
        List<Object> objs = args == null ? null : Arrays.asList(args);
        metaData.setArgs(objs);
        metaData.setParamTypes(transfer(method.getParameterTypes()));
        OutboundMsg msg = new OutboundMsg();
        msg.setType(RPC_REQUEST);
        msg.setArg(metaData);

        logger.info("metadata: {}", metaData);

        writeMsg(session, msg);

        // 等待服务器响应
        Object sync = new Object();
        rpcWaiters.put(requestId, sync);
        waitForServer(sync);
    }

    /**
     * 处理rpc响应
     *
     * @param addr
     * @param requestId
     * @param result
     */
    public void handleRpcResponse(String addr, Integer requestId, RpcResult result) {
        Session session = sessionMap.get(addr);
        session.putResult(requestId, result);
        logger.info("session: {}", session);
        // 唤醒对应rpc等待线程
        Object sync = rpcWaiters.get(requestId);
        notify(sync);
    }

    public void handleHeartBeat() {

    }

    /**
     * 等待服务器的响应
     * 等待对象为sync 分为以下几种:
     * 1. 如果请求为rpc请求，则对象保存在waitObjects的map中，映射为requestId --> obj
     * 2. 如果请求为session请求，则对象为service的class对象，因为一个消费者只会维持一个连接(单连接)[未来多连接可以扩展为一个map 由服务名+后缀的方式映射]
     *
     * @param sync
     */
    private void waitForServer(Object sync) {
        synchronized (sync) {
            try {
                sync.wait();
            } catch (InterruptedException e) {
                logger.error("interrupt when waiting for server");
            }
        }
    }

    /**
     * 唤醒对应等待的线程
     *
     * @param sync
     */
    public void notify(Object sync) {
        synchronized (sync) {
            sync.notify();
        }
    }

    /**
     * 将参数类型转换成它的类名
     *
     * @param paramTypes
     * @return
     */
    private List<String> transfer(Class[] paramTypes) {
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < paramTypes.length; ++i) {
            res.add(paramTypes[i].getCanonicalName());
        }

        return res;
    }

    /**
     * 将参数数组转化成参数列表
     *
     * @param objs
     * @return
     */
    private List<Object> getListArgs(Object[] objs){
        return null;
    }
    /**
     * 根据session获取对应的服务名
     *
     * @param session
     * @return
     */
    private String getServiceName(Session session) {
        Class service = session.getService();
        return service.getCanonicalName();
    }

    /**
     * 将session添加到sessionMap中
     *
     * @param session
     */
    public void addSession(String addr, Session session) {
        sessionMap.put(addr, session);
    }

    /**
     * 生成一个新的requestId
     */
    public Integer newRequestId() {
        return REQUESTID.get();
    }
}
