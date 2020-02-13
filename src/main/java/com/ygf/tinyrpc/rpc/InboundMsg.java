package com.ygf.tinyrpc.rpc;

/**
 * 入站消息的包装，即响应消息
 *
 * @author theo
 * @date 20181208
 */
public class InboundMsg {
    /**
     * 响应类型
     */
    private byte type;
    /**
     * 返回结果
     */
    private Object result;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
