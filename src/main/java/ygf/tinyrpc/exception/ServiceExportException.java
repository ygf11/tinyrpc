package ygf.tinyrpc.exception;

/**
 * 服务暴露时抛出的异常
 *
 * @author theo
 * @date 20190227
 */
public class ServiceExportException extends RuntimeException {
    public ServiceExportException(String message){
        super(message);
    }

    public ServiceExportException(String message, Throwable cause){
        super(message, cause);
    }
}
