package com.ygf.tinyrpc.context;

import com.ygf.tinyrpc.config.RpcConfigurations;
import com.ygf.tinyrpc.registry.ZooKeeperRegistry;
import com.ygf.tinyrpc.rpc.client.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
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
    private ConcurrentHashMap<Class, List<RpcProvider>> caches = new ConcurrentHashMap<Class, List<RpcProvider>>();
    /**
     * 发起rpc请求和接收请求的对象
     */
    private RpcClient rpcClient;
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
     * 获取对应服务对象
     *
     * @param service
     * @return
     */
    public Object getService(Class service) {
        // 直接从缓存中获取
        List<RpcProvider> providers = getProviders(service);
        providers = providers == null ? getProvidersFromRegistry(service) : providers;

        if (providers == null){
            return null;
        }

        // 获取其中的一个
        RpcProvider provider = loadBalanceByRandom(providers);
        Object proxy = provider.getProxy();
        if (proxy != null){
            return proxy;
        }

        // 与服务提供者尚未连接 则建立连接

        return null;
    }

    /**
     * 从注册中心获取providers
     *
     * @param service
     * @return
     */
    private List<RpcProvider> getProvidersFromRegistry(Class service) {
        /**
         * 1. 获取registry
         * 2. 检查/rpc/xxx/providers节点是否存在
         * 3. 获取/rpc/xxx/providers所有子节点
         * 4. 订阅/rpc/xxx/providers子节点的变化事件
         */
        String zkUrl = config.getRegistryConfig().getAddress();
        ZooKeeperRegistry registry = ZooKeeperRegistry.getInstance(zkUrl);

        // 从zk获取  并且监听进行异步更新
        String path = "/rpc/" + service.getCanonicalName() + "/providers";
        boolean exists = registry.exists(path);
        // 判断服务是否存在
        if (!exists) {
            logger.info("path {}, zk node not exists", path);
            return null;
        }

        List<RpcProvider> result;
        try {
            result = registry.getProviders(service);
        } catch (Exception e) {
            result = null;
            logger.error("get provider from registry error: {}", e);
        }

        return result;
    }

    /**
     * 当与zk断开连接时 从缓存中获取服务位置
     *
     * @param service
     * @return
     */
    private List<RpcProvider> getProviders(Class service) {
        return caches.get(service);
    }

    /**
     * 启动某个服务发现监听(利用zk的异步接口)
     *
     * @param cz
     */
    public void startDiscovery(Class cz) {


    }

    /**
     * 使用基于随机数的负载均衡策略
     *
     * @param list
     * @return
     */
    private RpcProvider loadBalanceByRandom(List<RpcProvider> list) {
        long current = System.currentTimeMillis();
        Random random = new Random(current);
        int next = random.nextInt(list.size());
        return list.get(next);
    }

    /**
     * 为服务创建代理对象
     *
     * @param provider
     * @return
     */
    private Object getProxy(RpcProvider provider) {

        return null;
    }
}
