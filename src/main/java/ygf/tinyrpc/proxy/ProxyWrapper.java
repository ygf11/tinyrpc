package ygf.tinyrpc.proxy;

import ygf.tinyrpc.rpc.client.RpcConnector;

/**
 * 代理对象的包装
 *
 * @author theo
 * @date 20190228
 */
public class ProxyWrapper {
    /**
     * 代理对象
     */
    private Object proxy;
    /**
     * 网络连接
     */
    private RpcConnector connector;

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public RpcConnector getConnector() {
        return connector;
    }

    public void setConnector(RpcConnector connector) {
        this.connector = connector;
    }
}
