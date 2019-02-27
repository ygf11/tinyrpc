package com.ygf.tinyrpc.config;

/**
 * 单个服务暴露的配置
 *
 * @author theo
 * @date 20190227
 */
public class ServiceConfig {
    /**
     * 应用配置
     */
    private ApplicationConfig application;
    /**
     * 注册中心配置
     */
    private RegistryConfig registry;
    /**
     * 协议配置
     */
    private ProtocolConfig protocol;
    /**
     * 服务暴露的接口
     */
    private String interfaceName;
    /**
     * 实际引用对象
     */
    private Object ref;

    public ApplicationConfig getApplication() {
        return application;
    }

    public void setApplication(ApplicationConfig application) {
        this.application = application;
    }

    public RegistryConfig getRegistry() {
        return registry;
    }

    public void setRegistry(RegistryConfig registry) {
        this.registry = registry;
    }

    public ProtocolConfig getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolConfig protocol) {
        this.protocol = protocol;
    }

    public String getInterface() {
        return interfaceName;
    }

    public void setInterface(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Object getRef() {
        return ref;
    }

    public void setRef(Object ref) {
        this.ref = ref;
    }
}
