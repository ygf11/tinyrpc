package com.ygf.tinyrpc.context;

import com.ygf.tinyrpc.config.ReferenceConfig;
import com.ygf.tinyrpc.config.ServiceConfig;
import com.ygf.tinyrpc.discovery.ServiceDiscovery;
import com.ygf.tinyrpc.export.Exporter;
import com.ygf.tinyrpc.loadBalance.RpcLoadBalance;
import com.ygf.tinyrpc.proxy.ProxyFactory;
import com.ygf.tinyrpc.proxy.ProxyWrapper;
import com.ygf.tinyrpc.rpc.client.RpcClient;
import org.apache.commons.collections4.CollectionUtils;
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
     * 保存的配置
     */
    private ConcurrentHashMap<String, String> configs = new ConcurrentHashMap<>();
    /**
     * 已经暴露的服务
     */
    private ConcurrentHashMap<Class, Object> exportedServices = new ConcurrentHashMap<>();
    /**
     * 保存服务接口-->代理服务的映射
     */
    private ConcurrentHashMap<Class, List<RpcProvider>> caches = new ConcurrentHashMap<Class, List<RpcProvider>>();
    /**
     * 发起rpc请求和接收请求的对象
     */
    private RpcClient rpcClient;
    /**
     * 服务暴露
     */
    private Exporter exporter;
    /**
     * 服务发现
     */
    private ServiceDiscovery discovery;
    /**
     * 负载均衡
     */
    private RpcLoadBalance loadBalance;
    /**
     * 单例对象
     */
    private static RpcContext instance;

    private RpcContext(){
        this.exporter = new Exporter();
        this.rpcClient = new RpcClient();
        this.discovery = new ServiceDiscovery();
        this.loadBalance = new RpcLoadBalance();
    }

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
     * 将指定服务进行暴露
     *
     * @param service
     */
    public void export(ServiceConfig service){
        exporter.export(service);
    }

    /**
     * 获取指定服务的远程代理
     *
     * @param reference
     */
    public Object get(ReferenceConfig reference){
        List<RpcProvider> providers = discovery.get(reference);
        if (CollectionUtils.isEmpty(providers)){
            logger.info("no exported service for {}", reference.getInterface());
            return null;
        }

        RpcProvider provider = loadBalance.getFromProviders(providers);

        // 创建代理对象
        ProxyWrapper proxy = provider.getProxy();
        if (proxy != null){
            return proxy;
        }

        proxy = ProxyFactory.createProxy(provider, rpcClient);
        provider.setProxy(proxy);
        return proxy.getProxy();
    }
}
