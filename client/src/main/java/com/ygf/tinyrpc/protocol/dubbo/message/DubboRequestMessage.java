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
     * 请求序列号标识
     */
    private int requestId;
    /**
     * 协议名
     */
    private String protocal;
    /**
     * 协议版本号
     */
    private byte version;
    /**
     * 请求类型
     */
    private byte type;
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

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getVersion() {
        return version;
    }
}