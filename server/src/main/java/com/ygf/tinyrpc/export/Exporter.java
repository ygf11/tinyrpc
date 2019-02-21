package com.ygf.tinyrpc.export;

import com.ygf.tinyrpc.rpc.server.RpcServerConnector;
import com.ygf.tinyrpc.service.ZooKeeperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务暴露类
 *
 * @author theo
 * @date 20190220
 */
public class Exporter {
    private static Logger logger = LoggerFactory.getLogger(Exporter.class);
    /**
     * 待暴露的服务
     */
    private List<ExporterInfo> infos = new ArrayList<ExporterInfo>();
    /**
     * 网络监听对象(单例) 所以不需要持有
     */
    private RpcServerConnector server;
    /**
     * 服务注册中心(但例) 所以不需要持有
     */
    private ZooKeeperRegistry registry;


    /**
     * 进行服务暴露
     */
    public void export() {
        /**
         * 1. 创建网络监听对象
         * 2. 开启网络监听
         * 3. 服务暴露，写入zk集群
         */
        String addr = getLocalAddr();
        Integer port = getPort();

        // 确保服务提供者端ip:port进行了配置
        if (addr == null || port == null) {
            logger.error("provider ip or port not config!");
            return;
        }

        RpcServerConnector connector = new RpcServerConnector(addr, port);
        // 启动监听
        connector.bootStrap();

        // 创建zk会话
        String url = getZkUrl();
        if (url == null) {
            logger.error("provider's zk url not config");
            return;
        }
        // TODO 超时等异常捕获
        ZooKeeperRegistry registry = ZooKeeperRegistry.getInstance(url);

        // 创建zk根节点失败
        if (!createRoot(registry)) {
            return;
        }

        for (ExporterInfo info : infos) {
            export(info, registry, addr, port);
        }

    }

    /**
     * 创建根节点
     *
     * @param registry
     */
    private boolean createRoot(ZooKeeperRegistry registry) {
        try {
            registry.createPersistent("/rpc", true);
            return true;
        } catch (Exception e) {
            logger.error("create /rpc failed");
        }

        return false;
    }

    /**
     * 创建服务节点
     *
     * @param registry
     */
    private boolean createExport(ZooKeeperRegistry registry, Class iService) {
        String path = "/rpc" + iService.getCanonicalName();
        try {
            registry.createPersistent(path, true);
            return true;
        } catch (Exception e) {
            logger.error("create " + path + " failed");
        }

        return false;
    }

    /**
     * 创建provider节点
     *
     * @param registry
     * @param provider
     */
    private boolean createProvider(ZooKeeperRegistry registry, String provider) {
        try {
            registry.createPersistent(provider, true);
            return true;
        } catch (Exception e) {
            logger.error("create " + provider + "failed");
        }

        return false;
    }

    /**
     * 暴露单个服务
     *
     * @param info
     * @param registry
     */
    private void export(ExporterInfo info, ZooKeeperRegistry registry, String addr, Integer port) {
        /**
         * 即在zk集群创建节点
         * 1. 检查接口服务节点是否存在，不存在则创建
         * 2. 检查接口节点下的provider和configuration是否存在，不存在则创建
         * 3. 在provider端创建对应节点
         */
        // 确保zk服务节点存在
        boolean success = createExport(registry, info.getService());
        if (!success) {
            return;
        }

        // 确保providers节点存在
        String providers = "/rpc/" + info.getService().getCanonicalName() + "/providers";
        success = createProvider(registry, providers);
        if (!success) {
            return;
        }

        // 向zk写入服务信息
        /**
         * 1. 协议名
         * 2. 服务所在ip:port
         * 3. 服务别名
         * 4. 服务所在应用
         * 5. 暴露的接口
         * 6. 暴露的方法
         */
        String canonicalName = info.getService().getCanonicalName();
        String url = "rpc://" + addr
                + "/" + info.getName()
                + "?" + "port=" + port
                + "?" + "appName=" + getAppName()
                + "?" + "interface=" + canonicalName
                + "?" + "methods=" + getMethods(info.getService());

        registry.createEphemeral(url);
    }

    /**
     * 获取所有非Object的接口方法
     *
     * @param service
     * @return
     */
    private String getMethods(Class service) {
        StringBuilder sb = new StringBuilder();
        Method[] methods = service.getMethods();
        for (Method method : methods) {
            sb.append(method.getName()).append(",");
        }

        return sb.toString().substring(0, sb.length() - 1);
    }

    /**
     * 获取本地ip地址
     *
     * @return
     */
    private String getLocalAddr() {
        /**
         * 1. 从配置中获取
         * 2. 未配置  则尝试获取
         * 3. 当前直接返回
         */

        return "192.168.1.101";
    }

    /**
     * 获取空闲网络监听的端口
     *
     * @return
     */
    private Integer getPort() {
        /**
         * 1. 从配置中获取
         * 2. 未配置 则尝试获取空闲
         * 3. 当前直接返回
         */

        return 20880;
    }

    /**
     * 获取zk集群的url
     *
     * @return
     */
    private String getZkUrl() {
        /**
         * 1. 从配置中获取
         * 2. 未配置 则抛异常
         * 3. 当前直接返回
         */

        return "127.0.0.1:2181";
    }

    /**
     * 获取应用名
     *
     * @return
     */
    private String getAppName() {
        /**
         * 1. 从配置中读取
         * 2. 未配置 则抛出异常
         * 3. 当前直接返回
         */

        return "app-test";
    }
}
