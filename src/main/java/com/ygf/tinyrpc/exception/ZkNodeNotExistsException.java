package com.ygf.tinyrpc.exception;

/**
 * zk节点不存在
 *
 * @author theo
 * @date 20190223
 */
public class ZkNodeNotExistsException extends RuntimeException {
    public ZkNodeNotExistsException(String message){
        super(message);
    }
}
