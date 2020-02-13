package com.ygf.tinyrpc.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * id生成器 在请求端使用 用于标识一个唯一的请求
 * 服务端返回结果时会带上这个请求id, 这样请求端可以根据这个标识将结果确定具体的调用方
 *
 * @author theo
 * @date 20181128
 */
public class IdGenerator {
    private AtomicInteger GENERATOR = new AtomicInteger();

    /**
     * 自增id生成器 当达到最大后返回0
     * 为了使用安全  只暴露这个方法
     *
     * @return
     */
    private int incrementAndGet() {
        int current;
        int next;
        do {
            current = GENERATOR.get();
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;
        } while (!GENERATOR.compareAndSet(current, next));

        return next;
    }

    /**
     * 生成一个id
     *
     * @return
     */
    public int get() {
        return incrementAndGet();
    }
}
