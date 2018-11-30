package com.ygf.tinyrpc.protocol.dubbo.message;

import static com.ygf.tinyrpc.protocol.dubbo.message.DubboProtocol.*;


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

    public byte getProtocol() {
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
}
