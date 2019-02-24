package com.ygf.tinyrpc.proxy;

import com.ygf.tinyrpc.context.RpcProvider;
import com.ygf.tinyrpc.rpc.client.RpcConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

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
     * @param provider
     * @return
     */
    public static Object createProxy(RpcProvider provider) {
        // service
        Class service = provider.getService();
        // classloader
        ClassLoader classLoader = getClassLoader(provider);
        // address
        InetSocketAddress address = getAddress(provider);

        RpcConnector connector = new RpcConnector(address);
        InvocationHandler handler = new RpcInvocationHandler(provider.getMethods(), connector);

        Object proxy =Proxy.newProxyInstance(classLoader, new Class[]{service.getClass()}, handler);

        // 启动网络连接
        try{
            connector.connect();
        }catch (Exception e){
            logger.error("connect to provider {} failed", provider);
        }

        return proxy;
    }

    /**
     * 根据provider获取classLoader
     *
     * @param provider
     * @return
     */
    private static ClassLoader getClassLoader(RpcProvider provider){
        Class service = provider.getService();
        return service.getClass().getClassLoader();
    }

    /**
     * 根据provider获取address
     *
     * @param provider
     * @return
     */
    private static InetSocketAddress getAddress(RpcProvider provider){
        String ip = provider.getIp();
        Integer port = provider.getPort();
        return new InetSocketAddress(ip, port);
    }

    /**
     * 开始启动与服务器的长连接
     *
     * @param connector
     */
    private void startSession(RpcConnector connector){

    }

}
