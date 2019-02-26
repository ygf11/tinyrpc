package com.ygf.tinyrpc.config.annotation;

/**
 * 属性键值对
 *
 * @author theo
 * @date 20190226
 */
public class Property {
    /**
     * 键
     */
    private String key;
    /**
     * 值
     */
    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Property(String key, String value){
        this.key = key;
        this.value = value;
    }


}
