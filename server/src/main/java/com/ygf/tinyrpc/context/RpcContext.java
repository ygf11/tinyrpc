package com.ygf.tinyrpc.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * rpc上下文
 *
 * @author theo
 * @date 20190220
 */
public class RpcContext {
    private static Logger logger = LoggerFactory.getLogger(RpcContext.class);
    /**
     * 保存服务接口-->代理服务的映射
     */
    private ConcurrentHashMap<Class, Object> service = new ConcurrentHashMap<Class, Object>();


}
