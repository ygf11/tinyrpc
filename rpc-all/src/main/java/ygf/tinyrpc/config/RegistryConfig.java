package ygf.tinyrpc.config;

/**
 * 注册中心配置
 *
 * @author theo
 * @date 20190227
 */
public class RegistryConfig {


    /**
     * 协议类型
     */
    private String type;
    /**
     * 注册中心地址
     */
    private String address;
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
