package com.ygf.tinyrpc.proxy;

import com.ygf.tinyrpc.rpc.client.RpcConnector;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理拦截类，包括服务暴露等主流程
 *
 * @author  theo
 * @date 20190220
 */
public class RpcInvocationHandler implements InvocationHandler {
    /**
     * 需要拦截的方法
     */
    private List<String> methods;
    /**
     * 网络监听对象
     */
    private RpcConnector connector;

    public RpcInvocationHandler(List<String> methods, RpcConnector connector){
        this.connector = connector;
        this.methods = methods;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /**
         *
         */
        return null;
    }
}
