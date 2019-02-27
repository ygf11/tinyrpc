package com.ygf.tinyrpc.annotations;

import java.lang.annotation.*;

/**
 * 应用信息
 *
 * @author  theo
 * @date 20190226
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AppName {
    String owner() default "";
}
