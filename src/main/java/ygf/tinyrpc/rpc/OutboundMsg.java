package ygf.tinyrpc.rpc;

/**
 * 出站请求的包装
 *
 * @author theo
 * @date 20181208
 */
public class OutboundMsg {
    /**
     * 请求类型
     */
    private byte type;
    /**
     * 请求参数对象
     */
    private Object arg;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Object getArg() {
        return arg;
    }

    public void setArg(Object arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        return type + " " + arg;
    }
}
