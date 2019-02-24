package com.ygf.tinyrpc.context;


import java.util.List;

/**
 * 从zk集群中读取的单个服务提供者信息(zk节点)
 *
 * @author theo
 * @date 20190221
 */
public class RpcProvider {
    /**
     * 服务所在ip
     */
    private String ip;
    /**
     * 服务别名
     */
    private String name;
    /**
     * 服务监听的端口
     */
    private Integer port;
    /**
     * 服务所在应用
     */
    private String appName;
    /**
     * 服务暴露的接口
     */
    private Class service;
    /**
     * 服务可以被调用的方法
     */
    private List<String> methods;
    /**
     * 服务rpc代理对象
     */
    private Object proxy;

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Class getService() {
        return service;
    }

    public void setService(Class service) {
        this.service = service;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }
}
