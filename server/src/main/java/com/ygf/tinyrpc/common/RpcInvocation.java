package com.ygf.tinyrpc.common;


import java.lang.reflect.Method;

/**
 * rpc调用信息
 *
 * @author theo
 * @date 20181202
 */
public class RpcInvocation {
    /**
     * 请求所在的sessionId
     */
    private Integer sessionId;
    /**
     * rpc请求标识
     */
    private Integer requestId;
    /**
     * 目标服务接口类
     */
    private Class target;
    /**
     * 目标方法类
     */
    private Method method;
    /**
     * 方法参数类型数组
     */
    private Class[] paramTypes;
    /**
     * 方法参数值对象数组
     */
    private Object[] args;

    public Integer getRequestId() {
        return requestId;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Class getTarget() {
        return target;
    }

    public void setTarget(Class target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }


    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "sessionId: " + sessionId +
                "requestId: " + requestId +
                "target: " + target.getCanonicalName() +
                "method: " + method.getName();
    }
}
