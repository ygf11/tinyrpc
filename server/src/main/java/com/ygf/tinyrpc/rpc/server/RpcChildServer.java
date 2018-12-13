package com.ygf.tinyrpc.rpc.server;

import com.ygf.tinyrpc.rpc.AbstractWriter;
import com.ygf.tinyrpc.rpc.InboundMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 服务器端与客户端通信的对等体
 *
 * @author theo
 * @date 20181212
 */
public class RpcChildServer extends AbstractWriter {

    private static Logger logger = LoggerFactory.getLogger(RpcChildServer.class);
    /**
     * 与io分离的线程池
     */
    private ThreadPoolExecutor executor;
    /**
     * 具体服务的配置
     */
    private Map<Class, Object> configMap = new ConcurrentHashMap<Class, Object>();
    /**
     * 整个应用级别的配置
     */
    private Object appConfig;
    /**
     * spring上下文
     */
    private ApplicationContext context;

    /**
     * 处理来自客户端的请求
     *
     * @param msg
     */
    public void handleRequest(InboundMsg msg) {
        if (msg == null) {
            logger.info("msg is null");
            return;
        }

        byte type = msg.getType();
        switch(type){
            case RPC_REQUEST:
                break;
            case EXIT_SESSION:
                break;
            case HEARTBEATS:
                break;
            case CREATE_SESSION_REQUEST:
                break;
             default:
                 logger.error("not supported request type:{}", type);
        }
    }

    /**
     * 为客户端创建会话
     *
     * @param msg
     */
    private void initChildSession(InboundMsg msg){

    }



}
