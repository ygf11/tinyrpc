package ygf.tinyrpc.protocol.jessie.common;

/**
 * 会话状态
 *
 * @author theo
 * @date 20181130
 */
public class SessionStatus {
    /**
     * 客户端正在与服务器进行连接
     */
    public static int CONNECTING = 1;
    /**
     * 服务器收到客户端的确认后
     */
    public static int CONNECTED = 2;
    /**
     * 客户端发出退出session请求，或者在一定时间内没有发送心跳后
     * 服务器端断开连接的状态
     */
    public static int DISCONNECT = 3;
}
