package com.ygf.tinyrpc.protocol.dubbo.message;

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
     * 方法参数对象
     */
    private List<Object> params;

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


}
