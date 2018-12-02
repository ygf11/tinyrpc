package com.ygf.tinyrpc.protocol.dubbo.common;

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

    private Session(){
        status = SessionStatus.DISCONNECT;
    }
}
