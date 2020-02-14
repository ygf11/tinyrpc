package ygf.tinyrpc.protocol.jessie.code;

import ygf.tinyrpc.protocol.jessie.message.*;

import static ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

import ygf.tinyrpc.serialize.SerializeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 字节流写入的编码器
 * TODO 抛异常的逻辑 服务器客户端编码解码代码拆分
 *
 * @author theo
 * @date 20181130
 */
public class MsgToByteEncoder extends MessageToByteEncoder<Header> {

    private static final Logger logger = LoggerFactory.getLogger(MsgToByteEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Header msg, ByteBuf out) {
        if (msg == null) {
            logger.warn("msg is null");
            return;
        }
        // 标记初始位置
        out.markWriterIndex();

        // 协议
        out.writeByte(msg.getProtocol());
        // 版本
        out.writeByte(msg.getVersion());
        // 请求类型
        out.writeByte(msg.getType());
        // sessionId
        out.writeInt(msg.getSessionId());

        int pos = out.writerIndex();
        out.writeInt(0);


        switch (msg.getType()) {
            case RPC_REQUEST:
                encodeForRpcRequest(msg, out, pos);
                break;
            case RPC_RESPONSE:
                encodeForRpcResponse(msg, out, pos);
                break;
            case CREATE_SESSION_REQUEST:
                encodeForInitSession(msg, out, pos);
                break;
            case CREATE_SESSION_RESPONSE:
                //case CREATE_SESSION_ACK:
                break;
            default:
                logger.error("type {} not support", msg.getType());
        }

    }

    /**
     * 客户端创建会话报文编码
     *
     * @param header
     * @param out
     * @param saved
     */
    private void encodeForInitSession(Header header, ByteBuf out, int saved) {
        boolean isInitSession = header instanceof InitSessionMessage;
        if (!isInitSession) {
            logger.warn("class and header type not matched");
            return;
        }

        // 写入appName
        InitSessionMessage msg = (InitSessionMessage) header;
        int length;
        length = writeString(msg.getService(), out);
        length += writeString(msg.getAppName(), out);

        updateLength(out, length, saved);
    }

    /**
     * 为rpc请求报文编码
     *
     * @param header
     * @param out
     * @param saved
     */
    private void encodeForRpcRequest(Header header, ByteBuf out, int saved) {
        boolean isRequest = header instanceof RpcRequestMessage;
        if (!isRequest) {
            logger.warn("request and header type not matched");
            return;
        }

        int length;
        RpcRequestMessage msg = (RpcRequestMessage) header;
        // requestId
        out.writeInt(msg.getRequestId());
        length = 4;
        // 目标方法
        length += writeString(msg.getMethod(), out);

        // 写入参数类型列表
        byte paramSize = (byte) msg.getParams().size();
        if (paramSize < 0){
            logger.error("param count illegal");
            return;
        }

        out.writeByte(paramSize);
        int count = 1;
        count += writeListInfo(msg.getParamTypes(), out);
        if (count < 0) {
            logger.error("write parameter types error!");
            return;
        }
        length += count;

        // 写入参数值列表
        count = writeListInfo(msg.getParams(), out);
        if (count < 0) {
            logger.error("write parameters error!");
            return;
        }
        length += count;

        updateLength(out, length, saved);

    }

    /**
     * rpc响应报文的编码
     *
     * @param header
     * @param out
     * @param saved
     */
    private void encodeForRpcResponse(Header header, ByteBuf out, int saved) {
        boolean isResponse = header instanceof RpcResponseMessage;
        if (!isResponse) {
            logger.warn("response and header type not matched");
            return;
        }

        int length;
        RpcResponseMessage msg = (RpcResponseMessage) header;
        // 写入requestId和resultType
        out.writeInt(msg.getRequestId());
        length = 4;
        // 结果类型(正常/异常 不需要 可以根据class进行判断)
        //out.writeByte(msg.getResultType());
        //length += 1;

        // 写入结果类型
        length += writeString(msg.getResultClass(), out);
        // 写入结果
        byte[] bytes;
        try {
            Object res = msg.getResult();
            bytes = SerializeUtils.objectToByteArray(res);
        } catch (Exception e) {
            out.resetWriterIndex();
            return;
        }
        out.writeShort(bytes.length);
        out.writeBytes(bytes, 0, bytes.length);
        length = length + 2 + bytes.length;

        updateLength(out, length, saved);
    }

    /**
     * 判断上游传过来的消息是否有效
     *
     * @param header
     * @return
     */
    private boolean isInvalid(Header header) {
        if (header == null || header.getType() == 0) {
            return false;
        }

        return true;
    }


    /**
     * 向out中写入rpc请求基本信息，服务名和请求id
     *
     * @param requestId
     * @param service
     * @param out
     * @return
     */
    private int writeRpcBaseInfo(int requestId, String service, ByteBuf out) {
        out.writeInt(requestId);
        byte[] bytes = service.getBytes();
        out.writeShort(bytes.length);
        out.writeBytes(bytes, 0, bytes.length);
        return 6 + bytes.length;
    }

    /**
     * 向out写入一个字节的数据，返回写入的长度
     *
     * @param data
     * @param out
     * @return
     */
    private int writeByte(byte data, ByteBuf out) {
        out.writeByte(data);
        return 1;
    }

    /**
     * 向out写入int类型的数据，返回写入的长度
     *
     * @param data
     * @param out
     * @return
     */
    private int writeInt(int data, ByteBuf out) {
        out.writeInt(data);
        return 4;
    }

    /**
     * 向out写入一个字符串 返回写入的长度
     *
     * @param str
     * @param out
     * @return
     */
    private int writeString(String data, ByteBuf out) {
        byte[] bytes = data.getBytes();
        out.writeShort(bytes.length);
        out.writeBytes(bytes, 0, bytes.length);
        return 2 + bytes.length;
    }

    /**
     * 向out写入一个list，并且返回当前写入的字节数
     *
     * @param list
     * @param out
     * @return
     */
    private int writeListInfo(List list, ByteBuf out) {
        byte count = (byte) (list.size());
        // 写入参数
        int length = 0;

        for (Object param : list) {
            try {
                byte[] bytes = SerializeUtils.objectToByteArray(param);
                out.writeShort(bytes.length);
                out.writeBytes(bytes, 0, bytes.length);
                length = length + 2 + bytes.length;
            } catch (Exception e) {
                out.resetWriterIndex();
                return -1;
            }
        }

        return length;
    }


    /**
     * 更新数据域的长度
     *
     * @param out
     * @param length
     * @param saved
     */
    private void updateLength(ByteBuf out, int length, int saved) {
        // 更新数据段长度
        out.markWriterIndex();
        out.writerIndex(saved);
        out.writeInt(length);
        out.resetWriterIndex();
    }
}
