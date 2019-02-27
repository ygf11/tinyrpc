package com.ygf.tinyrpc.annotations;

import java.lang.annotation.*;

/**
 * 作用在成员域上的注解 表示zk注册中心的地址
 *
 * @author theo
 * @date 20190226
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZkRegistryUrl {
    String type() default "zookeeper";
}
