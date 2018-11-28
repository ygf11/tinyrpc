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
     * 如果返回类型为抛出一个异常 则此项为异常类名  否则此项不存在
     */
    private String exceptionClassName;
    /**
     * 结果数据
     */
    private Object result;


}
