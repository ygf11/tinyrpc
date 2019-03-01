package com.ygf.tinyrpc.protocol.jessie.message;

/**
 * 客户端向服务器发送的创建会话请求报文
 *
 * @author theo
 * @date 20181207
 */
public class InitSessionMessage extends Header {
    /**
     * 应用名 客户端一个jvm进程(应用)对应一个会话
     */
    private String appName;
    /**
     * 对应的接口服务
     */
    private String service;

    public InitSessionMessage(){}
    public InitSessionMessage(Header header){
        super(header);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

}
