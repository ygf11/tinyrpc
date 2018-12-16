package com.ygf.tinyrpc.common;

/**
 * 服务器端rpc调用结果
 *
 * @author  theo
 * @date 20181216
 */
public class ServerRpcResult {
    /**
     * 请求id
     */
    private Integer requestId;
    /**
     * 结果类型
     */
    private Class resultType;
    /**
     * 结果
     */
    private Object result;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Class getResultType() {
        return resultType;
    }

    public void setResultType(Class resultType) {
        this.resultType = resultType;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
