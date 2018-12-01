package com.ygf.tinyrpc.protocol.dubbo.message;

/**
 * rpc响应报文
 *
 * @author theo
 * @date 20181130
 */
public class RpcResponseMessage  extends Header{
    /**
     * 表示rpc请求的id
     */
    private Integer requestId;
    /**
     * 客户端请求的服务名
     */
    private String service;
    /**
     * 返回类型 正常-1/异常-2
     */
    private Byte resultType;
    /**
     * 结果的全限定类名(当type为2时，是一个异常类名)
     */
    private String targetClass;
    /**
     * 结果对象
     */
    private Object result;

    public RpcResponseMessage(){}

    public RpcResponseMessage(Header header){
        super(header);
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Byte getResultType() {
        return resultType;
    }

    public void setResultType(byte resultType) {
        this.resultType = resultType;
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
