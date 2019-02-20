package com.ygf.tinyrpc.proxy;

/**
 * 代理服务对象
 *
 * @author theo
 * @date 20190220
 */
public class Proxy {
    /**
     * 由代理工厂创建的代理对象
     */
    private Object rpcProxy;
    /**
     * 对应的具体服务接口
     */
    private Class target;

    public Proxy(Object service, Class target) {
        this.rpcProxy = service;
        this.target = target;
    }
}
