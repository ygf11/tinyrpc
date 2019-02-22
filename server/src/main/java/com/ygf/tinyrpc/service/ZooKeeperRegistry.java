package com.ygf.tinyrpc.service;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 注册中心
 *
 * 1. 服务暴露
 * 2. 服务发现
 *
 * @author theo
 * @date 20190219
 */
public class ZooKeeperRegistry {
    /**
     * ip:port
     */
    private String url;
    /**
     * zk会话
     */
    private ZkClient zkClient;
    /**
     * 单例
     */
    private static ZooKeeperRegistry registry;

    private ZooKeeperRegistry(String url) {
        this.url = url;
        zkClient = new ZkClient(url, 5000);
    }

    public static ZooKeeperRegistry getInstance(String url) {
        if (registry == null) {
            synchronized (ZooKeeperRegistry.class) {
                if (registry == null) {
                    registry = new ZooKeeperRegistry(url);
                }
            }
        }

        return registry;
    }

    /**
     * 创建永久节点
     *
     * @param path
     * @param createParant
     */
    public void createPersistent(String path, boolean createParant) throws Exception {
        zkClient.createPersistent(path, true);
    }


    /**
     * 创建临时节点
     *
     * @param path
     */
    public void createEphemeral(String path) {
        zkClient.createEphemeral(path);
    }

    /**
     * 关闭registry
     */
    public void close() {
        zkClient.close();
    }

    /**
     * 异步获取服务url
     *
     * @param parent
     */
    public void subscribeChildChanges(String parent) {
        IZkChildListener listener = new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                Lock lock = new ReentrantLock();
                lock.lock();
                try {

                } catch (Exception e) {

                } finally {
                    lock.unlock();
                }
            }
        };

        zkClient.subscribeChildChanges(parent, listener);
    }

    /**
     * 从zk获取url(解析模块待做)
     *
     * @param path
     * @return
     */
    public String readData(String path){
        return zkClient.readData(path, true);
    }
}
