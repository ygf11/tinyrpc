package ygf.tinyrpc.protocol.jessie.message;


/**
 * dubbo协议头
 *
 * @author theo
 * @date 20181130
 */
public class Header {
    /**
     * 协议类型
     */
    private byte protocol;
    /**
     * 协议版本
     */
    private byte version;
    /**
     * 请求类型
     */
    private byte type;
    /**
     * 会话id
     */
    private int sessionId;

    public Header(){};

    public Header(Header header){
        this.protocol = header.getProtocol();
        this.version = header.getVersion();
        this.type = header.getType();
        this.sessionId = header.sessionId;
    }

    public byte getProtocol() {
        return protocol;
    }

    public void setProtocol(byte protocol) {
        this.protocol = protocol;
    }

    public Byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
