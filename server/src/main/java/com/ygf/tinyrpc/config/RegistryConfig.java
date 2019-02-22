package com.ygf.tinyrpc.config;

/**
 * 注册中心配置
 *
 * @author theo
 * @date 20190222
 */
public class RegistryConfig {
    /**
     * 协议--目前只支持zk
     */
    private String protocol;
    /**
     * 注册中心地址
     */
    private String address;

    public RegistryConfig(String protocol, String address){
        this.protocol = protocol;
        this.address = address;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
