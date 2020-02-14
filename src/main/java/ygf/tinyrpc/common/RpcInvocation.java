package ygf.tinyrpc.common;


import java.lang.reflect.Method;

/**
 * rpc调用信息
 *
 * @author theo
 * @date 20181202
 */
public class RpcInvocation {
    /**
     * 目标服务接口类
     */
    private Class target;
    /**
     * 目标方法类
     */
    private Method method;
    /**
     * 方法参数值对象数组
     */
    private Object[] args;

    public Class getTarget() {
        return target;
    }

    public void setTarget(Class target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "target: " + target.getCanonicalName() +
                "method: " + method.getName();
    }
}
