package com.ygf.tinyrpc.protocol.jessie.message;

import java.util.ArrayList;
import java.util.List;

/**
 * rpc请求报文
 *
 * @author theo
 * @date 20181130
 */
public class RpcRequestMessage extends Header {
    /**
     * 客户端一次rpc请求的标识
     */
    private int requestId;
    /**
     * 服务名(全限定类名+方法名)
     */
    private String service;
   /**
     * 方法参数类型
     */
    private List<String> paramTypes;
    /**
     * 方法参数对象
     */
    private List<Object> params;

    public RpcRequestMessage() {
        paramTypes = new ArrayList<String>();
        params = new ArrayList<Object>();
    }

    public RpcRequestMessage(Header header) {
        super(header);
        params = new ArrayList<Object>();
    }

    public int getRequestId() {
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

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }


    public List<String> getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(List<String> paramTypes) {
        this.paramTypes = paramTypes;
    }


}
