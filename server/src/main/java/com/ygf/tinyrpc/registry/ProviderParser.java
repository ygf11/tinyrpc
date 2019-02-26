package com.ygf.tinyrpc.registry;

import com.ygf.tinyrpc.context.RpcProvider;
import com.ygf.tinyrpc.exception.ProviderUrlParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * provider解析器
 * TODO 用一个hashMap保存对应下标-->内容解析类的映射
 *
 * @author theo
 * @date 20190222
 */
public class ProviderParser {
    private static Logger logger = LoggerFactory.getLogger(ProviderParser.class);
    /**
     * 协议所在下标
     */
    private static final Integer PROTOCOL_INDEX = 0;
    /**
     * ‘//’处的内容
     */
    private static final Integer EMPTY_INDEX = 1;
    /**
     * ip地址所在下标
     */
    private static final Integer IP_INDEX = 2;
    /**
     * 服务别名所在下标
     */
    private static final Integer NAME_INDEX = 3;
    /**
     * 键值对数据所在下标(port appName...)
     */
    private static final Integer KEY_VALUE_INDEX = 4;
    /**
     * 协议
     */
    private static final String PROTOCOL = "rpc::";
    /**
     * 端口
     */
    private static final String PORT = "port";
    /**
     * 应用名
     */
    private static final String APPNAME = "appName";
    /**
     * 暴露服务的接口
     */
    private static final String INTERFACE = "interface";
    /**
     * 暴露的方法
     */
    private static final String METHODS = "methods";
    /**
     * 端口在键值对数组中的下标
     */
    private static final Integer PORT_INDEX = 0;
    /**
     * 应用名在键值对数组中的下标
     */
    private static final Integer APPNAME_INDEX = 1;
    /**
     * 服务暴露的接口所在键值对数组的下标
     */
    private static final Integer INTERFACE_INDEX = 2;
    /**
     * 暴露的方法在键值对数组中的下标
     */
    private static final Integer METHODS_INDEX = 3;
    /**
     * provider字符串第一次解析时 数组的长度
     */
    private static final Integer INDEX_MAX = KEY_VALUE_INDEX;
    /**
     * key-value包含port appName interface method四项
     */
    private static final Integer KEY_VALUE_MAX = 4;
    /**
     * key-value使用'='分离，数组长度为2
     */
    private static final Integer KEY_VALUE_LENGTH = 2;
    /**
     * provider字符串第二次解析时 数组的长度
     */
    private static final Integer PROVIDER_ARRAY_SIZE_SECOND = 4;

    private static final Map<Integer, String> MAP = new HashMap<>(16);

    static {
        MAP.put(PORT_INDEX, PORT);
        MAP.put(APPNAME_INDEX, APPNAME);
        MAP.put(INTERFACE_INDEX, INTERFACE);
        MAP.put(METHODS_INDEX, METHODS);
    }

    public ProviderParser() {

    }

    /**
     * 将服务提供者信息解析成对象
     * <p>
     * url形式: rpc://ip/registry-name?port=x?appName=x?interface=x?methods=x,x,x
     * 1. 根据'/'符号解析为"rpc:" "ip" "" "registry-name.........." 四部分
     * 2. 检查"ip"的有效性
     * 3. 根据'?'符号解析余下部分
     * 4. 根据','符号解析'methods'
     * </p>
     *
     * @param provider
     * @return
     */
    public RpcProvider parse(String provider) throws ProviderUrlParseException, ClassNotFoundException {
        String[] array = provider.split("/");
        if (array.length <= INDEX_MAX + 1) {
            logger.error("provider {}, parse error", provider);
            throw new ProviderUrlParseException("provider url parse error");
        }

        if (!array[PROTOCOL_INDEX].equals(PROTOCOL)) {
            logger.error("provider {}, protocol is not rpc", provider);
            throw new ProviderUrlParseException("provider url parse error");
        }
        // ip
        String ip = array[IP_INDEX];
        checkIpValidate(ip);

        if (array[EMPTY_INDEX].length() != 0) {
            throw new ProviderUrlParseException("provider url parse error");
        }

        //服务别名
        String name = array[NAME_INDEX];

        String[] keyValues = array[KEY_VALUE_INDEX].split("//?");

        if (keyValues.length != KEY_VALUE_MAX) {
            logger.error("provider {}, key=value parse error", provider);
            throw new ProviderUrlParseException("Protocol url parse error");
        }

        // 解析键值对
        Map<Integer, String> map = new HashMap<>(16);
        for (int i = 0; i < keyValues.length; ++i) {
            String value = parseKeyValue(array[i], i);
            map.put(i, value);
        }

        String appName = map.get(APPNAME_INDEX);
        checkAppNameValidate(appName);

        Integer port = Integer.valueOf(map.get(PORT_INDEX));
        checkPortValidate(port);

        checkServiceValidate(map.get(INTERFACE_INDEX));
        Class service = findByName(map.get(INTERFACE_INDEX));

        String[] methods = map.get(METHODS_INDEX).split(",");
        checkMethodsValidate(methods);

        // 组装返回对象
        RpcProvider res = new RpcProvider();
        res.setIp(ip);
        res.setName(name);
        res.setPort(port);
        res.setAppName(appName);
        res.setService(service);
        res.setMethods(Arrays.asList(methods));

        return res;
    }


    /**
     * 解析键值对
     *
     * @param entries
     * @param index
     * @return
     * @throws ProviderUrlParseException
     */
    private String parseKeyValue(String entries, int index) throws ProviderUrlParseException {
        String[] map = entries.split("//?");
        if (map.length != KEY_VALUE_LENGTH) {
            throw new ProviderUrlParseException("num of key-value not correct");
        }

        String key = map[0];
        String value = map[1];

        if (!MAP.get(index).equals(key)) {
            throw new ProviderUrlParseException("key {}, is not correct");
        }

        return value;
    }

    /**
     * 通过类名查找class对象
     *
     * @param service
     * @return
     */
    private Class findByName(String service) throws ClassNotFoundException {
        return Class.forName(service);
    }

    /**
     * 检查ip有效性
     * TODO
     */
    private void checkIpValidate(String ip) {

    }

    /**
     * 检查应用名的有效性
     *
     * @param appName
     */
    private void checkAppNameValidate(String appName) {

    }

    /**
     * 检查端口的有效性
     *
     * @param port
     */
    private void checkPortValidate(Integer port) {

    }

    /**
     * 检查接口名的有效性
     *
     * @param service
     */
    private void checkServiceValidate(String service) {

    }

    /**
     * 检查接口方法名的有效性
     *
     * @param methods
     */
    private void checkMethodsValidate(String[] methods) {

    }
}
