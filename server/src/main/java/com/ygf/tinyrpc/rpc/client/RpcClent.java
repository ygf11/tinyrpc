package com.ygf.tinyrpc.rpc.client;

import com.ygf.tinyrpc.common.RpcResult;
import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;
import com.ygf.tinyrpc.rpc.OutboundMsg;
import com.ygf.tinyrpc.rpc.service.ServiceDiscovery;

import java.nio.channels.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消费者端协议无关的通信类
 *
 * @author theo
 * @date 20181209
 */
public class RpcClent {
    /**
     * 具体服务到对应配置的映射
     */
    private final Map<Class, Object> services = new ConcurrentHashMap<Class, Object>();
    /**
     * 服务请求通道
     */
    private final Map<Class, Channel> pipe = new ConcurrentHashMap<Class, Channel>();
    /**
     * rpc结果
     */
    private final Map<Integer, RpcResult> results = new ConcurrentHashMap<Integer, RpcResult>();
    /**
     * 应用级别的配置
     */
    private Object config;
    /**
     * 服务发现模块
     */
    private ServiceDiscovery serviceDiscovery;

    public RpcClent(){}

    public void initSession(String appName){
        OutboundMsg msg = new OutboundMsg();
        msg.setType(CREATE_SESSION_REQUEST);
        msg.setArg(appName);
    }



}
