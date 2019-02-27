package com.ygf.tinyrpc.config;

import com.ygf.tinyrpc.annotations.*;
import com.ygf.tinyrpc.config.annotation.AppNameParser;
import com.ygf.tinyrpc.config.annotation.ConfigParser;
import com.ygf.tinyrpc.config.annotation.Property;
import com.ygf.tinyrpc.config.annotation.ProtocolParser;
import com.ygf.tinyrpc.exception.ConfigurationParseException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置解析类
 *
 * @author theo
 * @date 20190226
 */
public class ConfigurationParser {
    /**
     * 应用名
     */
    public final static String APPLICATION_NAME = "appName";
    /**
     * 应用管理员
     */
    public final static String APP_OWNER = "owner";
    /**
     * 注册中心地址
     */
    public final static String ZKURL = "zkUrl";
    /**
     * 服务暴露域名
     */
    public final static String PROVIDER_HOST = "provider-host";
    /**
     * 服务暴露端口
     */
    public final static String PROVIDER_PORT = "provider-porT";
    /**
     * rpc协议
     */
    public final static String PROTOCOL = "protocol";
    /**
     * 表示这是一个需要创建代理的类
     */
    public final static String REFERENCE = "reference";
    /**
     * 保存所有需要解析的注解
     */
    private final List<Class> annotations = new ArrayList<>();
    private final Map<Class<? extends Annotation>, ConfigParser> map = new HashMap<>();

    public ConfigurationParser() {
        init();
    }

    /**
     * 放在非static方法中是为了减少内存的占用
     */
    private void init() {
        annotations.add(AppName.class);
        annotations.add(Protocol.class);
        annotations.add(Reference.class);
        annotations.add(RpcConfig.class);
        annotations.add(Service.class);
        annotations.add(ZkRegistryUrl.class);

        map.put(AppName.class, new AppNameParser());
        map.put(Protocol.class, new ProtocolParser());
    }

    /**
     * 解析注解
     *
     * @param config
     * @return
     */
    public Map<String, String> parse(Class config, Object obj) throws Exception {
        Field[] fields = config.getFields();
        if (fields == null || fields.length == 0) {
            throw new ConfigurationParseException("none config field");
        }

        List<Property> list = new ArrayList<>();
        for (Field field : fields) {
            Annotation annotation = findFirstAnnotation(field, annotations);
            if (annotation != null) {
                list.addAll(parseAnnotation(annotation, field, obj));
            }
        }

        Map<String, String> res = new HashMap<>(16);
        for (Property property : list) {
            res.put(property.getKey(), property.getValue());
        }
        return res;
    }

    /**
     * 扫描需要暴露/发现的服务
     *
     * @param packageName
     * @return
     */
    public List<Class> scan(String packageName) {
        return null;
    }

    /**
     * 返回列表中第一个修饰字段的注解
     *
     * @param field
     * @return
     */
    private Annotation findFirstAnnotation(Field field, List<Class> list) {

        for (Class type : list) {
            Annotation annotation = field.getAnnotation(type);
            if (annotation != null) {
                return annotation;
            }
        }

        return null;
    }


    private List<Property> parseAnnotation(Annotation annotation, Field field, Object object) throws Exception {
        Class type = annotation.annotationType();
        ConfigParser parser = map.get(type);
        return parser.parse(annotation, field, object);
    }
}
