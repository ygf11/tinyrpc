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
    private Class iService;
    /**
     * 真正的服务对象
     */
    private Object target;

    public ExporterInfo(Class iService, Object target){
        this.iService = iService;
        this.target = target;
    }

    public Class getiService() {
        return iService;
    }

    public void setiService(Class iService) {
        this.iService = iService;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
