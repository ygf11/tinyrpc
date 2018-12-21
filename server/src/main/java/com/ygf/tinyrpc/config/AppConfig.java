package com.ygf.tinyrpc.config;

/**
 * 应用配置 单例
 * //TODO 线程安全问题
 *
 * @author theo
 * @date 20181221
 */
public class AppConfig {
    /**
     * 应用名
     */
    private static String appName;
    /**
     * 应用管理员
     */
    private static String owner;

    public static String getAppName() {
        return appName;
    }

    public static void setAppName(String appName) {
        AppConfig.appName = appName;
    }

    public static String getOwner() {
        return owner;
    }

    public static void setOwner(String owner) {
        AppConfig.owner = owner;
    }
}
