package com.ygf.tinyrpc.protocol.dubbo.message;

/**
 * 请求返回消息
 * @author theo
 * @Date 20181127
 */
public class DubboResponseMessage {
    /**
     * 请求序号
     */
    private Integer requestId;
    /**
     * 协议类型
     */
    private String protocol;
    /**
     * 结果数据
     */
    private Object result;


}
