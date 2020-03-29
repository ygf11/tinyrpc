package ygf.tinyrpc.protocol.jessie.common;

/**
 * 服务器端的session
 *
 * @author theo
 * @date 20181213
 */
public class ServerSession  extends Session{

    private String appName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return super.toString()
                + " appName: " + appName;
    }

}
