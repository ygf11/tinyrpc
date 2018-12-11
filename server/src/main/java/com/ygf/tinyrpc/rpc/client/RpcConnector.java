package com.ygf.tinyrpc.rpc.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端网路连接启动类
 *
 * @author theo
 * @date 20181211
 */
public class RpcConnector {
    /**
     * 保存ip到对应连接的映射  用于解决单连接问题
     */
    private static final  Map<String, RpcConnector> connectorMap = new ConcurrentHashMap<String, RpcConnector>();

}
