package com.ygf.tinyrpc.config.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 解析服务提供者的协议字段
 *
 * @author theo
 * @date 20190226
 */
public class ProtocolParser implements ConfigParser {
    @Override
    public List<Property> parse(Annotation annotation, Field field, Object object) throws Exception {
        return null;
    }
}
