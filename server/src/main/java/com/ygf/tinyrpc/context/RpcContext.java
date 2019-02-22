package com.ygf.tinyrpc.context;

import com.ygf.tinyrpc.service.ZooKeeperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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
    private ConcurrentHashMap<Class, List<RpcProvider>> providers = new ConcurrentHashMap<Class, List<RpcProvider>>();
    /**
     * 单例对象
     */
    private static RpcContext instance;

    /**
     * 单例对象获取入口
     *
     * @return
     */
    public static RpcContext getInstance(){
        if (instance == null){
            synchronized (RpcContext.class){
                if (instance == null){
                    instance = new RpcContext();
                }
            }
        }

        return instance;
    }

    /**
     * rpc初始化入口
     * 1. 读取配置
     * 2. 服务暴露
     * 3. 允许消费者启动
     *
     */
    private void start(){

    }

    /**
     * 添加或者替换
     *
     * @param service
     * @param providers
     */
    public void addProviders(Class service, List<RpcProvider> providers){

    }

    /**
     * 当与zk断开连接时 从缓存中获取服务位置
     *
     * @param service
     * @return
     */
    public List<RpcProvider> getProviders(Class service){
        return null;
    }

    /**
     * 启动某个服务发现监听(利用zk的异步接口)
     *
     * @param cz
     */
    public void startDiscovery(Class cz){
        ZooKeeperRegistry registry = ZooKeeperRegistry.getInstance();
    }
}
