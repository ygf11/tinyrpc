package com.ygf.tinyrpc.proxy;

import com.ygf.tinyrpc.rpc.server.RpcServerConnector;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理拦截类，包括服务暴露等主流程
 *
 * @author  theo
 * @date 20190220
 */
public class RpcInvocationHandler implements InvocationHandler {
    /**
     * 网络监听对象
     */
    private RpcServerConnector connector;

    public RpcInvocationHandler(RpcServerConnector connector){
        this.connector = connector;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
