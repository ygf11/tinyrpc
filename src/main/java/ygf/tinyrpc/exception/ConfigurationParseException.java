package ygf.tinyrpc.exception;

/**
 * 配置文件解析异常
 *
 * @author theo
 * @date 20190226
 */
public class ConfigurationParseException extends RuntimeException {
    public ConfigurationParseException(String message){
        super(message);
    }
}
