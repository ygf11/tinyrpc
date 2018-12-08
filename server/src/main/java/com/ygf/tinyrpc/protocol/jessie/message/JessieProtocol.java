package com.ygf.tinyrpc.protocol.jessie.message;

/**
 * dubbo协议传输的格式
 *
 * @author theo
 * @date 20181129
 */
public class JessieProtocol {
    /**
     * dubbo协议类型
     */
    public static final byte PROTOCOL = 1;
    /**
     * header段长度
     */
    public static final byte HEADER_LENGTH = 11;
    /**
     * 当前协议版本
     */
    public static final byte CURRENT_VERSION = 1;
    /**
     * 协议+版本长度
     */
    public static final byte PROTOCOL_LEN = 2;


    //以下是请求类型
    /**
     * 客户端发送的创建会话类型
     */
    public static final byte CREATE_SESSION_REQUEST = 1;
    /**
     * 响应创建会话
     */
    public static final byte CREATE_SESSION_RESPONSE = 2;
    /**
     * 客户端对创建会话的确认
     */
    //public static final byte CREATE_SESSION_ACK = 3;
    /**
     * 客户端发送的心跳请求
     */
    public static final byte HEARTBEATS = 4;
    /**
     * 客户端发送的rpc请求
     */
    public static final byte RPC_REQUEST = 5;
    /**
     * rpc响应
     */
    public static final byte RPC_RESPONSE = 6;
    /**
     * 客户端发送的会话退出类型
     */
    public static final byte EXIT_SESSION = 7;
}
