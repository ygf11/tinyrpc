package com.ygf.tinyrpc.annotations;

/**
 * 标志服务消费者 需要为其生成代理
 *
 * @author theo
 * @date 20190226
 */
public @interface Reference {
    String id() default "";
    Class<?> service();
    /**
     * TODO 集群容错机制等
     */
}
