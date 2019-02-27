package com.ygf.tinyrpc.config.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import static com.ygf.tinyrpc.config.ConfigurationParser.*;
/**
 * 解析@Reference注解
 *
 * @author theo
 * @date 20190227
 */
public class ReferenceParser implements ConfigParser {
    @Override
    public List<Property> parse(Annotation annotation, Field field, Object object) throws IllegalAccessException {
        List<Property> result = new ArrayList<>();
        field.setAccessible(true);
        String reference = String.valueOf(field.get(object));
        result.add(new Property(REFERENCE, reference));
        return result;
    }
}
