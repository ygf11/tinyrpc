package com.ygf.tinyrpc.annotations;

import java.lang.annotation.*;

/**
 * 标志需要暴露的服务提供者
 *
 * @author theo
 * @date 20190226
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
}
