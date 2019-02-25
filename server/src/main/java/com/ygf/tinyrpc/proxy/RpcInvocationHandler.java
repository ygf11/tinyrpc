package com.ygf.tinyrpc.proxy;

import com.ygf.tinyrpc.exception.MethodNotExportedException;
import com.ygf.tinyrpc.protocol.jessie.common.Session;
import com.ygf.tinyrpc.rpc.client.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理拦截类，包括服务暴露等主流程
 *
 * @author theo
 * @date 20190220
 */
public class RpcInvocationHandler implements InvocationHandler {
    /**
     * 需要拦截的方法
     */
    private List<String> methods;
    /**
     * client
     */
    private RpcClient client;



    /**
     * 关联的会话
     */
    private Session session;

    public RpcInvocationHandler(List<String> methods, RpcClient client) {
        this.client = client;
        this.methods = methods;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        /**
         * 1. 对于暴露的方法 则进行拦截
         * 2. 否则抛出rpcException
         */
        String name = method.getName();
        // 方法不在暴露列表中 则抛出异常
        if (!methods.contains(name)){
            throw new MethodNotExportedException("method: "+ name+ "not exported");
        }

        // 否则开启rpc请求
        Integer requestId = client.newRequestId();
        client.rpcRequest(requestId, session, method, args);
        return session.getResult(requestId);
    }
}
