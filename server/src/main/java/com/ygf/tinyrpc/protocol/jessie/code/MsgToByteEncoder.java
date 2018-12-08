package com.ygf.tinyrpc.protocol.jessie.code;

import com.ygf.tinyrpc.protocol.jessie.message.*;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

import com.ygf.tinyrpc.serialize.SerializeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字节流写入的编码器
 * TODO 抛异常的逻辑
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
        int length = 0;
        byte[] appName = msg.getAppName().getBytes();
        out.writeShort(appName.length);
        out.writeBytes(appName, 0, appName.length);
        length = 2 + appName.length;

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

        int length = 0;
        RpcRequestMessage msg = (RpcRequestMessage) header;
        // 写入requestId和service
        length = writeRpcBaseInfo(msg.getRequestId(), msg.getService(), out);

        byte count = (byte) (msg.getParams().size());
        if (count < 0) {
            logger.error("sessionId {}, requestId {},rpc请求中参数个数大于127", msg.getSessionId(), msg.getRequestId());
            return;
        }
        // 写入参数
        out.writeByte(count);
        length += 1;
        for (Object param : msg.getParams()) {
            try {
                byte[] bytes = SerializeUtils.objectToByteArray(param);
                out.writeShort(bytes.length);
                out.writeBytes(bytes, 0, bytes.length);
                length = length + 2 + bytes.length;
            } catch (Exception e) {
                out.resetWriterIndex();
                return;
            }
        }

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

        int length = 0;
        RpcResponseMessage msg = (RpcResponseMessage) header;
        // 写入requestId和service
        length = writeRpcBaseInfo(msg.getRequestId(), msg.getService(), out);

        out.writeByte(msg.getResultType());
        length += 1;

        String target = msg.getTargetClass();
        out.writeShort(target.length());
        out.writeBytes(target.getBytes(), 0, target.length());
        length += msg.getTargetClass().length();
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
