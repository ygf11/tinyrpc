package com.ygf.tinyrpc.protocol.jessie.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话单例类
 *
 * @author theo
 * @date 20181202
 */
public class Session {
    /**
     * 会话当前状态
     */
    private int status;
    /**
     * 会话id
     */
    private Integer sessionId;
    /**
     * 结果集
     */
    private Map<Integer, Object> results = new ConcurrentHashMap<Integer, Object>();

    private static Session instance = new Session();

    private Session(){
        status = SessionStatus.DISCONNECT;
    }

    public static Session getInstance(){
        return instance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Object getResults(Integer requestId) {
        return results.get(requestId);
    }

    public void putResult(Integer requestId, Object result) {
        results.put(requestId, result);
    }
}
