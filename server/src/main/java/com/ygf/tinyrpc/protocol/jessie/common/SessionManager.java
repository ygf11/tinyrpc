package com.ygf.tinyrpc.protocol.jessie.common;

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
     */
    private static Map<Integer, ClientInfo> sessions = new ConcurrentHashMap<Integer, ClientInfo>();

    /**
     * 添加/更新会话
     * @param sessionId
     * @param info
     */
    public static void addSession(int sessionId, ClientInfo info){
        sessions.put(sessionId, info);
    }

    /**
     * 获取会话信息
     * @param sessionId
     * @return
     */
    public static ClientInfo getSession(int sessionId){
        return sessions.get(sessionId);
    }
}
