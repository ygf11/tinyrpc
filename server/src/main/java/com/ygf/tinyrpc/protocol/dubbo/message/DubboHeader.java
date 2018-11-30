package com.ygf.tinyrpc.protocol.dubbo.message;

/**
 * dubbo协议头
 *
 * @author theo
 * @date 20181130
 */
public class DubboHeader {
    /**
     * 协议类型
     */
    private byte protocol;
    /**
     * 协议版本
     */
    private byte version;
    /**
     * 请求类型
     */
    private byte type;
    /**
     * 会话id
     */
    private int sessionId;
    /**
     * 数据段长度
     */
    private int dataLength;

    public byte getProtocol() {
        return protocol;
    }

    public void setProtocol(byte protocol) {
        this.protocol = protocol;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getSessionId() {
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
