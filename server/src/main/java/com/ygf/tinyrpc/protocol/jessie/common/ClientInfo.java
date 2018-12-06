package com.ygf.tinyrpc.protocol.jessie.common;

import io.netty.channel.Channel;

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
     * 地址(ip:port)
     */
    private String addr;
    /**
     * 会话当前状态
     */
    private int status;
    /**
     * 与对方通信的channel
     */
    private Channel channel;

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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
