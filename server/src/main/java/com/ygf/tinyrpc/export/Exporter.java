package com.ygf.tinyrpc.export;

import com.ygf.tinyrpc.rpc.server.RpcServerConnector;
import com.ygf.tinyrpc.service.ZooKeeperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务暴露类
 *
 * @author theo
 * @date 20190220
 */
public class Exporter {
    private static Logger logger = LoggerFactory.getLogger(Exporter.class);
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

        String url = getUrl();
        ZooKeeperRegistry registry = ZooKeeperRegistry.getInstance(url);



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
     * @return
     */
    private String getUrl(){
        /**
         * 1. 从配置中获取
         * 2. 未配置 则抛异常
         * 3. 当前直接返回
         */

        return "127.0.0.1:2181";
    }

}
