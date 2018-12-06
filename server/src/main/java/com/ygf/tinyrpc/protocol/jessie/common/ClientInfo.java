package com.ygf.tinyrpc.protocol.jessie.common;

/**
 * 客户端信息
 *
 * @author theo
 * @date 20181206
 */
public class ClientInfo {
    /**
     * 会话id
     */
    private int sessionId;
    /**
     * 应用名
     */
    private String appName;
    /**
     * 对方ip地址
     */
    private String ip;
    /**
     * 通信端口
     */
    private Integer port;

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
