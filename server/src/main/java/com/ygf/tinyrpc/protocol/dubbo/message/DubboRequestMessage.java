package com.ygf.tinyrpc.protocol.dubbo.message;

import java.util.List;

/**
 * 请求端发送的消息
 *
 * @author theo
 * @Date 20181127
 */
public class DubboRequestMessage {
    /**
     * 服务名：全限定类名+方法名
     */
    private String serviceName;
    /**
     * 参数全限定类名
     */
    private List<String> paramNames;
    /**
     * 参数值
     */
    private List<Object> params;

    public DubboRequestMessage(String serviceName, List<String> paramNames, List<Object> params) {
        this.serviceName = serviceName;
        this.paramNames = paramNames;
        this.params = params;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setParamNames(List<String> paramNames) {
        this.paramNames = paramNames;
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public List<Object> getParams() {
        return params;
    }
}
