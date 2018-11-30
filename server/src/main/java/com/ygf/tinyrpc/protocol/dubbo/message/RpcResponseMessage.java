package com.ygf.tinyrpc.protocol.dubbo.message;

/**
 * rpc响应报文
 *
 * @author theo
 * @date 20181130
 */
public class RpcResponseMessage {
    /**
     * 表示rpc请求的id
     */
    private int requestId;
    /**
     * 客户端请求的服务名
     */
    private String sevice;
    /**
     * 返回类型 正常-1/异常-2
     */
    private byte type;
    /**
     * 结果的全限定类名(当type为2时，是一个异常类名)
     */
    private String targetClass;
    /**
     * 结果对象
     */
    private Object result;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getSevice() {
        return sevice;
    }

    public void setSevice(String sevice) {
        this.sevice = sevice;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }




}
