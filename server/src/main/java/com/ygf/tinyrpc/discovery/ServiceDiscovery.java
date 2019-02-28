package com.ygf.tinyrpc.discovery;

import com.ygf.tinyrpc.config.ReferenceConfig;
import com.ygf.tinyrpc.context.RpcProvider;
import com.ygf.tinyrpc.exception.ServiceDiscoveryException;
import com.ygf.tinyrpc.proxy.ProxyWrapper;
import com.ygf.tinyrpc.registry.ZooKeeperRegistry;
import com.ygf.tinyrpc.rpc.client.RpcConnector;
import org.I0Itec.zkclient.IZkChildListener;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 服务发现类 用于实现服务发现功能
 *
 * @author theo
 * @date 20190228
 */
public class ServiceDiscovery {
    private Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);
    /**
     * 保存服务接口-->代理服务的映射
     */
    private Map<String, List<RpcProvider>> caches = new ConcurrentHashMap<>();
    /**
     * 保存zkUrl-->注册中心的映射
     */
    private Map<String, ZooKeeperRegistry> registryMap = new ConcurrentHashMap<>();
    /**
     * zk节点解析器
     */
    private ProviderParser parser;

    public ServiceDiscovery() {
        this.parser = new ProviderParser();
    }

    /**
     * 获取远程代理对象
     *
     * @param reference
     * @return
     */
    public List<RpcProvider> get(ReferenceConfig reference) {
        /**
         * 1. 从缓存获取，存在则直接返回
         * 2. 缓存不存在，则从zk获取 解析 创建代理对象
         * 3. 缓存不存在，还需要进行该远程服务的事件订阅
         */

        List<RpcProvider> providers = caches.get(reference.getInterface());
        // 缓存为空
        if (CollectionUtils.isEmpty(providers)) {
            providers = getProvidersFromRegistry(reference);
        }

        return providers;
    }

    /**
     * 从zk中获取providers
     *
     * @return
     */
    private List<RpcProvider> getProvidersFromRegistry(ReferenceConfig reference) {
        String zkUrl = reference.getRegistry().getAddress();
        // 首先查看是否已经存在zk会话
        ZooKeeperRegistry registry = registryMap.get(zkUrl);
        if (registry == null) {
            registry = new ZooKeeperRegistry(zkUrl);
            registryMap.put(zkUrl, registry);
        }

        // path
        String path = "/rpc/" + reference.getInterface() + "/providers";
        boolean exists = registry.exists(path);

        if (!exists) {
            throw new ServiceDiscoveryException(path + "not exists in zk");
        }

        // 订阅异步异步更新
        IZkChildListener listener = (parentPath, currentChildren) -> {
            updateCaches(reference.getInterface(), parentPath, currentChildren);
        };
        List<String> providers = registry.getChildren(path);
        registry.subscribeChildChanges(path, listener);

        return providers.stream().map(x -> parser.parse(x)).collect(Collectors.toList());
    }

    private void updateCaches(String iName, String parent, List<String> providers){
        List<RpcProvider> oldProviders = caches.get(iName);
        List<RpcProvider> newProviders = providers.stream().map(x -> parser.parse(x))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(oldProviders)) {
            caches.put(iName, newProviders);
            return;
        }

        // 将不存在的服务 断开连接
        oldProviders.stream().forEach(x -> {
            int index = newProviders.indexOf(x);
            ProxyWrapper proxy = x.getProxy();
            // 更新proxy
            if (index != -1 && proxy != null){
                RpcProvider provider = newProviders.get(index);
                provider.setProxy(proxy);
            }else{
                cleanCache(x);
            }

        });

        caches.put(iName, newProviders);

    }

    /**
     * 清理无效的缓存
     * TODO
     *
     * @param cache
     */
    private void cleanCache(RpcProvider cache){
        RpcConnector connector = cache.getProxy().getConnector();
    }

}
