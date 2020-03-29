package ygf.tinyrpc.exception;

/**
 * 消费者调用没有暴露的远程方法时抛出的异常
 *
 * @author theo
 * @date 20190225
 */
public class MethodNotExportedException extends RuntimeException {
    public MethodNotExportedException(String message) {
        super(message);
    }

    public MethodNotExportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
