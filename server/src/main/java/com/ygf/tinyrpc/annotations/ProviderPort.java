package com.ygf.tinyrpc.annotations;

import java.lang.annotation.*;

/**
 * 服务暴露的端口
 *
 * @author theo
 * @date 20190226
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProviderPort {
    String port() default "";
}
