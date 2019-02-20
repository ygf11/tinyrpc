package com.ygf.tinyrpc.export;

/**
 * 服务暴露所需要的信息
 *
 * @author theo
 * @date 20190220
 */
public class ExporterInfo {
    /**
     * 服务暴露类接口
     */
    private Class target;
    /**
     * 服务网络监听的端口
     */
    private Integer port;

    public ExporterInfo(Class target, Integer port) {
        this.target = target;
        this.port = port;
    }

    public Class getTarget() {
        return target;
    }

    public void setTarget(Class target) {
        this.target = target;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "target: " + target.getCanonicalName() +
                "port: " + port;
    }
}
