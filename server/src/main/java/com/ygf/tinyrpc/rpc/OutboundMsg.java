package com.ygf.tinyrpc.rpc;

/**
 * 出站请求的包装
 *
 * @author theo
 * @date 20181208
 */
public class OutboundMsg {
    /**
     * rpc请求/响应所的会话
     */
    private Integer sessionId;
    /**
     * 请求类型
     */
    private byte type;
    /**
     * 请求参数对象
     */
    private Object arg;

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Object getArg() {
        return arg;
    }

    public void setArg(Object arg) {
        this.arg = arg;
    }
}
