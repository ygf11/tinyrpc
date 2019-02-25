package com.ygf.tinyrpc.proxy;

import com.ygf.tinyrpc.context.RpcProvider;
import com.ygf.tinyrpc.protocol.jessie.common.Session;
import com.ygf.tinyrpc.rpc.client.RpcClient;
import com.ygf.tinyrpc.rpc.client.RpcConnector;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

import static com.ygf.tinyrpc.protocol.jessie.common.SessionStatus.*;

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
    public static Object createProxy(RpcProvider provider, RpcClient client) {
        // service
        Class service = provider.getService();
        // classloader
        ClassLoader classLoader = getClassLoader(provider);
        // address
        InetSocketAddress address = getSocketAddress(provider);

        RpcConnector connector = new RpcConnector(address, client);
        InvocationHandler handler = new RpcInvocationHandler(provider.getMethods(), client);

        Object proxy = Proxy.newProxyInstance(classLoader, new Class[]{service.getClass()}, handler);

        // 启动网络连接
        try {
            Session session = startSession(connector, provider);
            ((RpcInvocationHandler) handler).setSession(session);
        }catch (Exception e){
            logger.error("init client for provider failed: {}", e);
            proxy = null;
        }
        return proxy;
    }

    /**
     * 根据provider获取classLoader
     *
     * @param provider
     * @return
     */
    private static ClassLoader getClassLoader(RpcProvider provider) {
        Class service = provider.getService();
        return service.getClass().getClassLoader();
    }

    /**
     * 根据provider获取address
     *
     * @param provider
     * @return
     */
    private static InetSocketAddress getSocketAddress(RpcProvider provider) {
        String ip = provider.getIp();
        Integer port = provider.getPort();
        return new InetSocketAddress(ip, port);
    }

    /**
     * 获取字符串的地址ip:port
     *
     * @param provider
     * @return
     */
    private static String getAddress(RpcProvider provider) {
        String ip = provider.getIp();
        Integer port = provider.getPort();
        return ip + ":" + port;
    }

    /**
     * 开始启动与服务器的长连接
     *
     * @param connector
     */
    private static Session startSession(RpcConnector connector, RpcProvider provider) throws Exception {
        /**
         * 1. 创建session 加入到rpcClient的addr->session的映射中
         * 2. 与服务器建立连接
         * 3. 与服务器建立session会话
         */
        // 建立连接
        ChannelFuture future = connector.connect();
        // 创建session
        RpcClient client = connector.getClient();
        String address = getAddress(provider);
        Session session = new Session();
        session.setService(provider.getService());
        session.setStatus(CONNECTING);

        // 添加到两个map中
        client.addSession(address, session);
        client.registerChannel(session, future.channel());

        // 建立会话 并且等待唤醒
        client.initSession(session, provider.getAppName());

        return session;
    }

}
