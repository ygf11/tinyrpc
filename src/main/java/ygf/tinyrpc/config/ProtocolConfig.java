package ygf.tinyrpc.config;

/**
 * 协议配置
 *
 * @author theo
 * @date 20190227
 */
public class ProtocolConfig {
    /**
     * 协议名称
     */
    private String name;
    /**
     * 服务暴露端域名
     */
    private String host;
    /**
     * 服务暴露端口
     */
    private Integer port;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
