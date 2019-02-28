package com.ygf.tinyrpc.utils;

import com.ygf.tinyrpc.config.ReferenceConfig;
import com.ygf.tinyrpc.config.ServiceConfig;
import com.ygf.tinyrpc.context.RpcContext;
import com.ygf.tinyrpc.context.RpcProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * rpc上下文工具类
 *
 * @author theo
 * @date 20190221
 */
public class RpcContextUtils {
    private static Logger logger = LoggerFactory.getLogger(RpcContextUtils.class);

    /**
     * 根据接口，获取具体的代理(因为暴露的服务都是单例的)
     *
     * @param cz
     */
    public static Object getService(Class service) {
        /**
         * 1. 服务url缓存都会通过异步更新
         * 2. 采用随机的方式进行负载均衡
         * 3. 直接从缓存中获取url
         * 4. 如果缓存为空，则尝试同步获取
         */
        RpcContext context = RpcContext.getInstance();
        return context.getService(service);
    }

    /**
     * 将指定service进行暴露
     *
     * @param service
     */
    public static void export(ServiceConfig service) {
        RpcContext.getInstance().export(service);
    }

    /**
     * 获取远程服务的代理
     *
     * @param ref
     */
    public static Object get(ReferenceConfig ref) {
        return RpcContext.getInstance().get(ref);
    }
}
