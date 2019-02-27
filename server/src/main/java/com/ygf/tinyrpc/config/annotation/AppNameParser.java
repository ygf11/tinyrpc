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
        List<Property> properties = new ArrayList<>();
        AppName appName = (AppName) annotation;
        //owner
        String owner = appName.owner();
        properties.add(new Property(APP_OWNER, owner));

        //name
        properties.add(getAppName(field, obj));
        return properties;
    }

    private Property getAppName(Field field, Object obj) throws IllegalAccessException{
        field.setAccessible(true);
        String name = String.valueOf(field.get(obj));
        return new Property(APPLICATION_NAME, name);
    }
}
