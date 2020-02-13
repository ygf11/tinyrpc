package com.ygf.tinyrpc.exception;

/**
 * 服务提供者信息解析异常
 *
 * @author theo
 * @date 20190222
 */
public class ProviderUrlParseException extends RuntimeException {
    public ProviderUrlParseException(String message){
        super(message);
    }

    public ProviderUrlParseException(Throwable cause) {
        super(cause);
    }

    public ProviderUrlParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
