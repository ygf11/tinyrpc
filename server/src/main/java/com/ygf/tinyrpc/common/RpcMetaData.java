package com.ygf.tinyrpc.common;

import java.util.List;

/**
 * rpc请求元数据 使用简单类表示
 *
 * @author theo
 * @date 20181214
 */
public class RpcMetaData {
    /**
     * rpc请求所在的sessionId
     */
    private Integer sessionId;
    /**
     * rpc请求对应的requestId
     */
    private Integer requestId;
    /**
     * 服务权限定类名
     */
    private String service;
    /**
     * 服务方法名
     */
    private String method;
    /**
     * 方法参数类型
     */
    private List<String> paramTypes;
    /**
     * 方法参数值
     */
    private List<Object> args;

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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<String> getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(List<String> paramTypes) {
        this.paramTypes = paramTypes;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "sessionId: " + sessionId +
                " requestId: " + requestId +
                " registry: " + service +
                " method: " + method +
                " paramTypes: " + paramTypes +
                " args: " + args;

    }


}
