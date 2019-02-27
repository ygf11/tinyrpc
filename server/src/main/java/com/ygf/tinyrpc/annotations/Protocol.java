package com.ygf.tinyrpc.annotations;

import java.lang.annotation.*;

/**
 * 使用的协议 目前只支持rpc
 *
 * @author theo
 * @date 20190226
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Protocol {
    String host() default "";
    String port() default "";
}
