package com.ygf.tinyrpc.proxy;

import com.ygf.tinyrpc.export.ExporterInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代理对象工厂
 * 在consumer端配合rpcContext一起进行服务调用
 *
 * @author theo
 * @date 20190220
 */
public class ProxyFactory {
    private static Logger logger = LoggerFactory.getLogger(ProxyFactory.class);

    /**
     * 创建代理对象
     *
     * @param info
     * @return
     */
    public static Proxy createProxy(ExporterInfo info){
        return null;
    }
}
