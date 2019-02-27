package com.ygf.tinyrpc.config.annotation;

import com.ygf.tinyrpc.annotations.Protocol;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.ygf.tinyrpc.config.ConfigurationParser.*;

/**
 * 解析服务提供者的协议字段
 *
 * @author theo
 * @date 20190226
 */
public class ProtocolParser implements ConfigParser {
    @Override
    public List<Property> parse(Annotation annotation, Field field, Object obj) throws IllegalAccessException {
        List<Property> properties = new ArrayList<>();
        // host port
        Protocol protocol = (Protocol) annotation;
        String host = protocol.host();
        String port = protocol.port();
        properties.add(new Property(PROVIDER_HOST, host));
        properties.add(new Property(PROVIDER_PORT, port));
        // protocol
        properties.add(getProtocol(field, obj));

        return properties;
    }

    private Property getProtocol(Field field, Object obj) throws IllegalAccessException{
        field.setAccessible(true);
        String type = String.valueOf(field.get(obj));
        return new Property(PROTOCOL, type);
    }
}
