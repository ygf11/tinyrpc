package com.ygf.tinyrpc.protocol.dubbo.message;

/**
 * 请求返回消息
 * @author theo
 * @date 20181127
 */
public class DubboResponseMessage {
    /**
     * 请求序号
     */
    private int requestId;
    /**
     * 协议类型
     */
    private String protocol;
    /**
     * 协议版本
     */
    private int version;
    /**
     * 返回类型 0-正常 1-抛出异常
     */
    private int type;
    /**
     * 当type为1 即抛出异常时使用 表示这个异常的全限定类名
     * 当type不为1时 此项不存在
     */
    private String exceptionClassName;
    /**
     * 结果数据
     */
    private Object result;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExceptionClassName() {
        return exceptionClassName;
    }

    public void setExceptionClassName(String exceptionClassName) {
        this.exceptionClassName = exceptionClassName;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
