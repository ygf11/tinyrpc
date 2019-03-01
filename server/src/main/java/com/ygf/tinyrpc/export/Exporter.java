package com.ygf.tinyrpc.export;

import com.ygf.tinyrpc.config.ProtocolConfig;
import com.ygf.tinyrpc.config.ServiceConfig;
import com.ygf.tinyrpc.exception.ServiceExportException;
import com.ygf.tinyrpc.rpc.server.RpcServerConnector;
import com.ygf.tinyrpc.registry.ZooKeeperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务暴露类
 *
 * @author theo
 * @date 20190220
 */
public class Exporter {
    private static Logger logger = LoggerFactory.getLogger(Exporter.class);
    /**
     * 多协议
     */
    private ConcurrentHashMap<ProtocolConfig, RpcServerConnector> protocolMaps = new ConcurrentHashMap<>();
    /**
     * 保存当前url=>对应注册中心的映射(注册中心可能有多个)
     */
    private ConcurrentHashMap<String, ZooKeeperRegistry> registryMaps = new ConcurrentHashMap<>();

    public Exporter() {
    }

    /**
     * 暴露单个服务
     *
     * @param service
     */
    public void export(ServiceConfig service) throws ServiceExportException {
        /**
         * 1. 创建网络监听对象
         * 2. 开启网络监听
         * 3. 服务暴露，写入zk集群
         */
        // 监听地址
        String host = service.getProtocol().getHost();
        Integer port = service.getProtocol().getPort();
        String iName = service.getInterface().getCanonicalName();

        // 确保服务提供者端ip:port进行了配置
        if (host == null || port == null) {
            logger.error("provider ip or port not config!");
            throw new ServiceExportException("protocol host or ip not config");
        }

        RpcServerConnector connector = new RpcServerConnector(host, port);
        connector.bootStrap();

        // 创建zk会话
        String zkUrl = service.getRegistry().getAddress();
        if (zkUrl == null) {
            logger.error("provider's zk url not config");
            throw new ServiceExportException("registry url not config");
        }

        ZooKeeperRegistry registry = registryMaps.get(zkUrl);
        if (registry == null) {
            registry = new ZooKeeperRegistry(zkUrl);
        }

        try {
            // 确保根节点存在
            registry.createPersistent("/rpc", true);

            // 在写入zk前 先保存
            protocolMaps.put(service.getProtocol(), connector);
            registryMaps.put(zkUrl, registry);

            // 写入zk
            doExport(service, registry);
            connector.completeExport(iName, service.getRef());
        }catch (Exception e){
            logger.error("export service {} error", service.getInterface());
            throw new ServiceExportException("export " + service.getInterface() + "exception", e);
        }

    }


    private void doExport(ServiceConfig service, ZooKeeperRegistry registry) throws Exception{
        /**
         * 即在zk集群创建节点
         * 1. 检查接口服务节点是否存在，不存在则创建
         * 2. 检查接口节点下的provider和configuration是否存在，不存在则创建
         * 3. 在provider端创建对应节点
         */
        String iName = service.getInterface().getCanonicalName();
        // 确保zk服务节点存在
        String servicePath = "/rpc/" + iName;
        registry.createPersistent(servicePath, true);

        // 确保providers节点存在
        String providers = "/rpc/" + iName + "/providers";
        registry.createPersistent(providers, true);

        // 向zk写入服务信息
        /**
         * 1. 协议名
         * 2. 服务所在ip:port
         * 3. 服务别名
         * 4. 服务所在应用
         * 5. 暴露的接口
         * 6. 暴露的方法
         */
        String ip = service.getProtocol().getHost();
        Integer port = service.getProtocol().getPort();
        String appName = service.getApplication().getApplicationName();
        String url = "rpc://" + ip
                + "/" + iName
                + "?" + "port=" + port
                + "?" + "appName=" + appName
                + "?" + "interface=" + iName
                + "?" + "methods=" + getMethods(iName);

        registry.createEphemeral(providers + "/" + URLEncoder.encode(url, "utf-8"));
    }


    /**
     * 获取所有非Object的接口方法
     *
     * @param iName
     * @return
     */
    private String getMethods(String iName) throws ClassNotFoundException {
        Class cz = Class.forName(iName);
        StringBuilder sb = new StringBuilder();
        Method[] methods = cz.getMethods();
        for (Method method : methods) {
            sb.append(method.getName()).append(",");
        }

        return sb.toString().substring(0, sb.length() - 1);
    }

}
