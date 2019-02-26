package com.ygf.tinyrpc.config.annotation;

import com.ygf.tinyrpc.annotations.AppName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import static com.ygf.tinyrpc.config.ConfigurationParser.*;


/**
 * 应用名注解解析
 *
 * @author theo
 * @date 20190226
 */
public class AppNameParser implements ConfigParser {
    @Override
    public List<Property> parse(Annotation annotation, Field field, Object obj) throws IllegalAccessException{
        if (!(annotation instanceof AppName)){
            return EMPTY;
        }
        List<Property> result = new ArrayList<>();
        String appName = String.valueOf(field.get(obj));
        Property property = new Property(APPLICATIONNAME, appName);

        result.add(property);
        return result;
    }
}
