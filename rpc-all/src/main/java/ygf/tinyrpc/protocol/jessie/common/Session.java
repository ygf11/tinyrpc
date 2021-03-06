package ygf.tinyrpc.protocol.jessie.common;

import ygf.tinyrpc.common.RpcResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端会话类 会话会在三个地方被修改：
 * 1. TCP连接之后 创建会话对象
 * 2. 客户端接收响应会话  修改会话状态
 * 3. 销毁会话  清除会话
 * <p>
 * 所以这三个地方需要加独占锁
 *
 * @author theo
 * @date 20181202
 */
public class Session {
    /**
     * 对应服务类
     */
    private Class service;
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
    private Map<Integer, RpcResult> results = new ConcurrentHashMap<Integer, RpcResult>();

    public Session() {
        status = SessionStatus.DISCONNECT;
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

    public RpcResult getResult(Integer requestId) {
        return results.get(requestId);
    }

    public void putResult(Integer requestId, RpcResult result) {
        results.put(requestId, result);
    }

    public Class getService() {
        return service;
    }

    public void setService(Class service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "registry: " + service
                + " status: " + status
                + " sessionId: " + sessionId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj instanceof Session) {
            return sessionId.equals(((Session) obj).sessionId);
        }

        return false;
    }
}
