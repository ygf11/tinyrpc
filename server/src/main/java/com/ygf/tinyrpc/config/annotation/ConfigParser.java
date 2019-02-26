package com.ygf.tinyrpc.config.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置解析
 *
 * @author theo
 * @date 20190226
 */
public interface ConfigParser {
    /**
     * 当注解不匹配时 返回这个空对象
     */
    List<Property> EMPTY = new ArrayList<>();

    /**
     * 根据不同的实现解析具体的注解
     *
     * @param annotation
     * @return
     */
    List<Property> parse(Annotation annotation, Field field, Object object) throws Exception;
}
