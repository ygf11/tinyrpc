package com.ygf.tinyrpc.common;

import java.util.List;

/**
 * rpc请求元数据 使用简单类表示
 *
 * @author theo
 * @date 20181214
 */
public class RpcMetaData {
    /**
     * rpc请求所在的sessionId
     */
    private Integer sessionId;
    /**
     * rpc请求对应的requestId
     */
    private Integer requestId;
    /**
     * 服务名：权限定类名+方法名
     */
    private String service;
    /**
     * 方法参数类型
     */
    private List<String> paramTypes;
    /**
     * 方法参数值
     */
    private List<Object> args;


}
