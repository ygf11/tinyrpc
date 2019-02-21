package com.ygf.tinyrpc.export;

import com.ygf.tinyrpc.rpc.server.RpcServerConnector;
import com.ygf.tinyrpc.service.ZooKeeperRegistry;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void export() throws Exception {
        /**
         * 1. 创建网络监听对象
         * 2. 开启网络监听
         * 3. 服务暴露，写入zk集群
         */
        Integer port = getPort();
        RpcServerConnector connector = new RpcServerConnector(port);
        // 启动监听
        connector.bootStrap();

        // 创建zk会话
        String url = getUrl();
        ZooKeeperRegistry registry = ZooKeeperRegistry.getInstance(url);

        // 创建zk根节点
        createRoot(registry);

        for (ExporterInfo info : infos) {
            export(info, registry);
        }
    }

    /**
     * 创建根节点
     *
     * @param registry
     */
    private void createRoot(ZooKeeperRegistry registry) {
        try {
            registry.createPersistent("/rpc", true);
        } catch (KeeperException.NodeExistsException e) {
            logger.info("/rpc node exists");
        }
    }



    /**
     * 暴露单个服务
     *
     * @param info
     * @param registry
     */
    private void export(ExporterInfo info, ZooKeeperRegistry registry) {
        /**
         * 即在zk集群创建节点
         * 1. 检查接口服务节点是否存在，不存在则创建
         * 2. 检查接口节点下的provider和configuration是否存在，不存在则创建
         * 3. 在provider端创建对应节点
         */




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
    private String getUrl() {
        /**
         * 1. 从配置中获取
         * 2. 未配置 则抛异常
         * 3. 当前直接返回
         */

        return "127.0.0.1:2181";
    }

}
