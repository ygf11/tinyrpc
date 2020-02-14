package ygf.tinyrpc.config;

/**
 * 应用配置
 *
 * @author theo
 * @date 20190227
 */
public class ApplicationConfig {
    /**
     * 应用名
     */
    private String applicationName;
    /**
     * 应用管理者
     */
    private String owner;


    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
