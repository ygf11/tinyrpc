package com.ygf.tinyrpc.protocol.dubbo.message;

/**
 * dubbo协议头
 *
 * @author theo
 * @date 20181130
 */
public class Header {
    /**
     * 协议类型
     */
    private Byte protocol;
    /**
     * 协议版本
     */
    private Byte version;
    /**
     * 请求类型
     */
    private Byte type;
    /**
     * 会话id
     */
    private Integer sessionId;
    /**
     * 数据段长度
     */
    private Integer dataLength;

    public Byte getProtocol() {
        return protocol;
    }

    public void setProtocol(byte protocol) {
        this.protocol = protocol;
    }

    public Byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public Byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }
}
