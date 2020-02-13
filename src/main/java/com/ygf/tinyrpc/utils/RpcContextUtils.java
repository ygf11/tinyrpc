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
     * 将指定service进行暴露
     *
     * @param service
     */
    public static void export(ServiceConfig service) {
        RpcContext.getInstance().export(service);
    }

    /**
     * 获取远程服务的代理(服务发现入口)
     *
     * @param ref
     */
    public static Object get(ReferenceConfig ref) {
        return RpcContext.getInstance().get(ref);
    }
}
