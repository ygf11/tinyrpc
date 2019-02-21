package com.ygf.tinyrpc.export;

/**
 * 表示待暴露的服务
 *
 * @author theo
 * @date 20190220
 */
public class ExporterInfo {
    /**
     * 服务暴露类接口
     */
    private Class service;
    /**
     * 真正的服务对象
     */
    private Object target;
    /**
     * 服务本地别名
     */
    private String name;

    public ExporterInfo(Class service, Object target) {
        this.service = service;
        this.target = target;
    }

    public Class getService() {
        return service;
    }

    public void setService(Class service) {
        this.service = service;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
