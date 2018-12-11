package com.ygf.tinyrpc.rpc.client;

import com.ygf.tinyrpc.common.RpcResult;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

import com.ygf.tinyrpc.rpc.AbstractClient;
import com.ygf.tinyrpc.rpc.OutboundMsg;
import com.ygf.tinyrpc.rpc.service.ServiceDiscovery;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消费者端协议无关的通信类
 *
 * @author theo
 * @date 20181209
 */
public class RpcClent extends AbstractClient {
    /**
     * 具体服务到对应配置的映射
     */
    private Map<Class, Object> services = new ConcurrentHashMap<Class, Object>();
    /**
     * 服务请求通道
     */
    private Map<Class, Channel> pipe = new ConcurrentHashMap<Class, Channel>();
    /**
     * rpc结果
     */
    private Map<Integer, RpcResult> results = new ConcurrentHashMap<Integer, RpcResult>();
    /**
     * 应用级别的配置
     */
    private Object config;
    /**
     * 服务发现模块
     */
    private ServiceDiscovery serviceDiscovery;

    public RpcClent() {
    }

    /**
     * 初始化
     */
    public void init(){

    }

    /**
     * 初始化会话
     *
     * @param service
     * @param appName
     */
    public void initSession(Class service, String appName) {
        // 服务发现
        // 负载均衡
        // 建立连接(单连接时 要在rpcConnector中判断是否存在对应连接  存在则不需要建立连接)
        // 创建会话
        OutboundMsg msg = new OutboundMsg();
        msg.setType(CREATE_SESSION_REQUEST);
        msg.setArg(appName);
        writeMsg(service, msg);
    }

    public void handleSessionInit(){

    }


}
