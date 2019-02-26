package com.ygf.tinyrpc.annotations;

import java.lang.annotation.*;

/**
 * 服务提供者域名
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProviderHost {
    String host() default "";
}
