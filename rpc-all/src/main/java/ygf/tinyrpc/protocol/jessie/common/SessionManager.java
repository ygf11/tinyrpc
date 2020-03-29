package ygf.tinyrpc.protocol.jessie.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器端会话管理类
 *
 * @author theo
 * @date 20181206
 */
public class SessionManager {
    /**
     * 服务器端保存的会话信息
     * 保存地址(ip:port)-->clientInfo的映射
     */
    private static Map<String, ClientInfo> sessions;

    static {
        sessions = new ConcurrentHashMap<String, ClientInfo>();
    }

    /**
     * 添加/更新会话
     *
     * @param addr
     * @param info
     */
    public static void addSession(String addr, ClientInfo info) {
        sessions.put(addr, info);
    }

    /**
     * 获取会话信息
     *
     * @param addr
     * @return
     */
    public static ClientInfo getSession(String addr) {
        return sessions.get(addr);
    }
}
