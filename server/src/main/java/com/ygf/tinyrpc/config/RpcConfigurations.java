package com.ygf.tinyrpc.config;

/**
 * 存放rpc的所有配置
 *
 * @author theo
 * @date 20181211
 */
public class RpcConfigurations {
    /**
     * 应用配置
     */
    private AppConfig appConfig;
    /**
     * 服务提供者的配置
     */
    private ProviderConfig providerConfig;
    /**
     * 注册中心的配置
     */
    private RegistryConfig registryConfig;
    /**
     * 消费者的配置
     */
    private ConsumerConfig consumerConfig;

    public RpcConfigurations(){}

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public ProviderConfig getProviderConfig() {
        return providerConfig;
    }

    public void setProviderConfig(ProviderConfig providerConfig) {
        this.providerConfig = providerConfig;
    }

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    public ConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    public void setConsumerConfig(ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }


}
