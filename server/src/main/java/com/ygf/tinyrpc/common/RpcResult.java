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
    int type;
    /**
     * 结果类
     */
    Class resultClass;
    /**
     * 结果对象
     */
    Object result;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Class getResultClass() {
        return resultClass;
    }

    public void setResultClass(Class resultClass) {
        this.resultClass = resultClass;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
