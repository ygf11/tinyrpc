package ygf.tinyrpc.common;

/**
 * 会话创建请求需要的数据
 *
 * @author theo
 * @date 20181214
 */
public class InitParams {
    /**
     * session连接对应的服务
     */
    private String service;
    /**
     * 当前
     */
    private String appName;

    public InitParams(){};

    public InitParams(String service, String appName){
        this.service = service;
        this.appName = appName;
    }


    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}

