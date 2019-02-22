package com.ygf.tinyrpc.config;

/**
 * 服务提供者的配置
 *
 * @author theo
 * @date 20181211
 */
public class ProviderConfig {
    /**
     * 网卡地址
     */
    private String addr;
    /**
     * 网卡端口
     */
    private Integer port;

    public ProviderConfig(String addr, Integer port){
        this.addr = addr;
        this.port = port;
    }
    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }



}
