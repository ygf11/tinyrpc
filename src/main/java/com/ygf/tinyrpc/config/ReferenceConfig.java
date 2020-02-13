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
     * 服务暴露的接口
     */
    private Class interfaceName;

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


    public Class getInterface() {
        return interfaceName;
    }

    public void setInterface(Class interfaceName) {
        this.interfaceName = interfaceName;
    }

}
