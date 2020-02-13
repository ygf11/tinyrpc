package com.ygf.tinyrpc.exception;

/**
 * 服务发现时出现的异常
 *
 * @author theo
 * @date 20190228
 */
public class ServiceDiscoveryException extends RuntimeException{
    public ServiceDiscoveryException(String messgae){
        super(messgae);
    }

    public ServiceDiscoveryException(String message, Throwable cause){
        super(message, cause);
    }
}
