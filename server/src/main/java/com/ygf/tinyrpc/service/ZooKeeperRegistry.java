package com.ygf.tinyrpc.service;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.KeeperException;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 服务发现模块
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
    public void createPersistent(String path, boolean createParant) throws KeeperException.NodeExistsException {

    }

    /**
     * 创建临时节点
     *
     * @param path
     */
    public void createEphemeral(String path) {

    }

    /**
     * 关闭registry
     */
    public void close() {
        zkClient.close();
    }

    /**
     * 订阅子节点变化的事件(更新缓存)
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



}
