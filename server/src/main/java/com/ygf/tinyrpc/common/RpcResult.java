package com.ygf.tinyrpc.common;

/**
 * rpc的结果：
 * 1. 正常情况返回一个对象
 * 2. 服务器端抛出异常，则返回异常对象
 *
 * @author theo
 * @date 20181206
 */
public class RpcResult {
    /**
     * 异常 正常
     */
    byte type;
    /**
     * 结果类
     */
    String resultType;
    /**
     * 结果对象
     */
    Object result;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
