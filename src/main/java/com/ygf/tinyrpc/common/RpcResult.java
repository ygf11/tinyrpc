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
     * 这个请求所属session的sessionId
     */
    private Integer sessionId;
    /**
     * rpc响应对应的请求的id
     */
    private Integer requestId;
    /**
     * 结果类
     */
    private String resultType;
    /**
     * 结果对象
     */
    private Object result;


    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }


    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
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


    @Override
    public String toString() {
        return "requestId: " + requestId
                + " returnType: " + resultType + " "
                + " result: " + result;
    }
}
