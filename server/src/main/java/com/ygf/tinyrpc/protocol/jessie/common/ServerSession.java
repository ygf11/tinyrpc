package com.ygf.tinyrpc.protocol.jessie.common;

/**
 * 服务器端的session
 *
 * @author theo
 * @date 20181213
 */
public class ServerSession  extends Session{
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    private String appName;

}
