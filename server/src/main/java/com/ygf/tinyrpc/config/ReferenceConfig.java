package com.ygf.tinyrpc.config;

/**
 *  服务消费者配置
 *
 * @author theo
 * @date 20190227
 */
public class ReferenceConfig {
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

}
