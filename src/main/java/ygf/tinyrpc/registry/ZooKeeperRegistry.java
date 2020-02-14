package ygf.tinyrpc.registry;

import ygf.tinyrpc.context.RpcProvider;
import ygf.tinyrpc.discovery.ProviderParser;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册中心
 * <p>
 * 1. 服务暴露
 * 2. 服务发现
 * </p>
 *
 * @author theo
 * @date 20190219
 */
public class ZooKeeperRegistry {

    private static Logger logger = LoggerFactory.getLogger(ZooKeeperRegistry.class);
    /**
     * ip:port
     */
    private String url;
    /**
     * zk会话
     */
    private ZkClient zkClient;
    /**
     * provider解析对象
     */
    private ProviderParser parser;
    /**
     * 单例
     */
    private static ZooKeeperRegistry registry;

    /**
     * 仅仅用于测试
     *
     * @param url
     * @param test
     */
    public ZooKeeperRegistry(String url, boolean test){
        this.url = url;
        if (!test){
            zkClient = new ZkClient(url);
        }
    }

    public ZooKeeperRegistry(String url) {
        this.url = url;
        zkClient = new ZkClient(url, 5000);
        this.parser = new ProviderParser();
    }

    /**
     * 创建永久节点
     *
     * @param path
     * @param createParents
     */
    public void createPersistent(String path, boolean createParents) {
        zkClient.createPersistent(path, createParents);
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
     * 订阅事件
     *
     * @param parent
     */
    public void subscribeChildChanges(String parent, IZkChildListener listener) {
        zkClient.subscribeChildChanges(parent, listener);
    }

    /**
     * 检查某个节点是否存在
     *
     * @param path
     * @return
     */
    public boolean exists(String path) {
        return zkClient.exists(path);
    }

    /**
     * 获取某个路径下的所有节点
     *
     * @param path
     * @return
     */
    public List<String> getChildren(String path){
       return zkClient.getChildren(path);
    }

    /**
     * 根据接口获取所有暴露的服务
     *
     * @param cz
     * @return
     */
    public List<RpcProvider> getProviders(Class cz) throws Exception {
        String path = "/rpc/" + cz.getCanonicalName() + "/providers";
        List<String> providers = zkClient.getChildren(path);

        List<RpcProvider> res = new ArrayList<>();
        for (String provider : providers) {
            res.add(parser.parse(provider));
        }
        return res;
    }


}
