package com.ygf.tinyrpc.protocol.jessie.code;

import com.ygf.tinyrpc.protocol.jessie.message.RpcRequestMessage;
import com.ygf.tinyrpc.protocol.jessie.message.RpcResponseMessage;
import com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol;
import com.ygf.tinyrpc.serialize.SerializeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import com.ygf.tinyrpc.protocol.jessie.message.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字节流写入的编码器
 *
 * @author theo
 * @date 20181130
 */
public class MsgToByteEncoder extends MessageToByteEncoder<Header> {

    private static final Logger logger = LoggerFactory.getLogger(MsgToByteEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Header msg, ByteBuf out) throws Exception {
        if (msg == null) {
            logger.warn("msg is null, {}", msg);
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

        // 非rpc请求和响应报文
        if (!isRpc(msg)) {
            return;
        }

        switch (msg.getType()) {
            case JessieProtocol.RPC_REQUEST:
                encodeForRequest(msg, out, pos);
                break;
            case JessieProtocol.RPC_RESPONSE:
                encodeForResponse(msg, out, pos);
                break;
            default:
                logger.error("sessionId {}, session msg's datalength is not 0", msg.getSessionId(), msg.getType());
        }

    }

    /**
     * rpc请求时的编码
     *
     * @param header
     * @param out
     * @param saved
     */
    private void encodeForRequest(Header header, ByteBuf out, int saved) {
        boolean isRequest = header instanceof RpcRequestMessage;
        if (!isRequest) {
            logger.warn("request and header type not matched");
            return;
        }

        RpcRequestMessage msg = (RpcRequestMessage) header;
        String service = msg.getService();

        int length = 0;
        out.writeInt(msg.getRequestId());
        length += 4;
        out.writeShort(service.length());
        length += 2;
        out.writeBytes(service.getBytes(), 0, service.length());
        length += service.length();

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
            }catch (Exception e){
                out.resetWriterIndex();
                return;
            }
        }

        // 更新数据段长度
        out.markWriterIndex();
        out.writerIndex(saved);
        out.writeInt(length);
        out.resetWriterIndex();

    }

    /**
     * rpc响应时的编码
     *
     * @param header
     * @param out
     * @param saved
     */
    private void encodeForResponse(Header header, ByteBuf out, int saved) {
        boolean isResponse = header instanceof RpcResponseMessage;
        if (!isResponse) {
            logger.warn("response and header type not matched");
            return;
        }

        RpcResponseMessage msg = (RpcResponseMessage) header;
        String service = msg.getService();
        int length = 0;

        out.writeInt(msg.getRequestId());
        length += 4;

        out.writeShort(service.length());
        length += 2;

        out.writeBytes(service.getBytes(), 0, service.length());
        length += service.length();

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
        }catch (Exception e){
            out.resetWriterIndex();
            return;
        }
        out.writeShort(bytes.length);
        out.writeBytes(bytes, 0, bytes.length);
        length = length + 2 + bytes.length;

        // 更新数据段长度
        out.markWriterIndex();
        out.writerIndex(saved);
        out.writeInt(length);
        out.resetWriterIndex();
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
     * 判断报文是否是rpc请求/rpc响应报文
     *
     * @return
     */
    private boolean isRpc(Header header) {
        byte type = header.getType();
        if (type == JessieProtocol.RPC_REQUEST || type == JessieProtocol.RPC_RESPONSE) {
            return true;
        }

        return false;
    }
}