package com.ygf.tinyrpc.context;

import com.ygf.tinyrpc.config.RpcConfigurations;
import com.ygf.tinyrpc.exception.ProviderUrlParseException;
import com.ygf.tinyrpc.registry.ZooKeeperRegistry;
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
     * rpc配置
     */
    private RpcConfigurations config;
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
    public static RpcContext getInstance() {
        if (instance == null) {
            synchronized (RpcContext.class) {
                if (instance == null) {
                    instance = new RpcContext();
                }
            }
        }

        return instance;
    }

    /**
     * rpc初始化入口
     * <p>
     * 1. 读取配置
     * 2. 服务暴露
     * 3. 允许消费者启动
     */
    private void start() {

    }

    /**
     * 添加或者替换
     *
     * @param service
     * @param providers
     */
    public void addProviders(Class service, List<RpcProvider> providers) {

    }

    /**
     * 当与zk断开连接时 从缓存中获取服务位置
     *
     * @param service
     * @return
     */
    public List<RpcProvider> getProviders(Class service) throws Exception {
        /**
         * 1. 获取registry
         * 2. 检查/rpc/xxx/providers节点是否存在
         * 3. 获取/rpc/xxx/providers所有子节点
         * 4. 订阅/rpc/xxx/providers子节点的变化事件
         */
        String zkUrl = config.getRegistryConfig().getAddress();
        ZooKeeperRegistry registry = ZooKeeperRegistry.getInstance(zkUrl);


        List<RpcProvider> cache = providers.get(service);
        // 如果缓存中存在 则直接返回缓存
        if (cache != null) {
            return cache;
        }

        // 否则从zk获取  并且监听进行异步更新
        String path = "/rpc/" + service.getCanonicalName() + "/providers";
        boolean exists = registry.exists(path);
        // 判断服务是否存在
        if (!exists) {
            logger.info("path {}, zk node not exists", path);
            return null;
        }

        try {
            return registry.getProviders(service);
        } catch (Exception e) {
            throw new Exception("can not get providers", e);
        }
    }

    /**
     * 启动某个服务发现监听(利用zk的异步接口)
     *
     * @param cz
     */
    public void startDiscovery(Class cz) {


    }
}
