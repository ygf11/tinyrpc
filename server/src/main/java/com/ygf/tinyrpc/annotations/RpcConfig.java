package com.ygf.tinyrpc.annotations;

import java.lang.annotation.*;

/**
 * 这个注解标识rpc的配置文件
 *
 * @author theo
 * @date 20190226
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcConfig {
}
