package com.ygf.tinyrpc.rpc.Exception;

/**
 * rpc调用异常
 *
 * @author theo
 * @date 20181216
 */
public class RpcException extends RuntimeException {
    public RpcException() {
        super();
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
