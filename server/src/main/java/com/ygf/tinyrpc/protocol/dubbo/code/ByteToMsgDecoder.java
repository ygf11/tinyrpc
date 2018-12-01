package com.ygf.tinyrpc.protocol.dubbo.code;

import com.ygf.tinyrpc.protocol.dubbo.message.Header;
import com.ygf.tinyrpc.protocol.dubbo.message.RpcRequestMessage;
import com.ygf.tinyrpc.protocol.dubbo.message.RpcResponseMessage;
import com.ygf.tinyrpc.serialize.SerializeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ygf.tinyrpc.protocol.dubbo.message.DubboProtocol.*;

import java.util.List;


/**
 * 字节流读取的解码器
 *
 * @author theo
 * @date 20181130
 */
public class ByteToMsgDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(ByteToMsgDecoder.class);
    private static final byte DUBBO_PROTOCOL = 1;


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 报文数据不够
        if (in.readableBytes() < HEADER_LENGTH) {
            return;
        }
        // 保留位置 以便回滚
        in.markReaderIndex();

        byte protocol = in.readByte();

        // 如果不是dubbo的数据报文  则不进行解析
        if (protocol != PROTOCOL) {
            in.readerIndex(0);
            return;
        }

        Header header = new Header();
        header.setProtocol(DUBBO_PROTOCOL);
        header.setVersion(in.readByte());
        header.setType(in.readByte());
        header.setSessionId(in.readInt());


        switch (header.getType()) {
            case RPC_REQUEST:
                parseRpcRequestPacket(in, header, out);
                break;
            case RPC_RESPONSE:
                parseRpcResponsePacket(in, header, out);
                break;
            case CREATE_SESSION_REQUEST:
            case CREATE_SESSION_RESPONSE:
            case CREATE_SESSION_ACK:
            case EXIT_SESSION:
            case HEARTBEATS:
                parseGeneralPacket(in, header, out);
                break;
            default:
                logger.error("dubbo not support type");
        }
    }

    /**
     * 处理通用的msg
     *
     * @param in
     * @param out
     */
    private void parseGeneralPacket(ByteBuf in, Header header, List<Object> out) {
        int length = in.readInt();
        assert length == 0;
        out.add(header);
    }

    /**
     * 解析rpc请求报文
     *
     * @param in
     * @param header
     * @param out
     */
    private void parseRpcRequestPacket(ByteBuf in, Header header, List<Object> out) {
        RpcRequestMessage msg = new RpcRequestMessage(header);
        int length = in.readInt();

        // 报文数据不够
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        msg.setRequestId(in.readInt());

        int serviceLength = in.readShort();
        byte[] serviceArray = new byte[serviceLength];
        in.readBytes(serviceArray, 0, serviceLength);
        msg.setService(new String(serviceArray));

        byte paramSize = in.readByte();

        for (byte i = 0; i < paramSize; ++i) {
            short len = in.readShort();
            byte[] bytes = new byte[len];
            in.readBytes(bytes, 0, len);
            Object param = null;
            try {
                param = SerializeUtils.byteArrayToObject(bytes);
                msg.getParams().add(param);
            } catch (Exception e) {
                // 跳过这个报文
                skipPacket(in, length);
                logger.error("byte array to obj error: {}", e);
                return;
            }
        }

        out.add(msg);
    }

    /**
     * 解析rpc响应报文
     *
     * @param in
     * @param header
     * @param out
     */
    private void parseRpcResponsePacket(ByteBuf in, Header header, List<Object> out) {
        RpcResponseMessage msg = new RpcResponseMessage(header);
        int length = in.readInt();

        // 报文数据不够
        if (in.readableBytes() < length) {
            in.readerIndex(0);
            return;
        }

        msg.setRequestId(in.readInt());

        short serviceLength = in.readShort();
        byte[] serviceArray = new byte[serviceLength];
        in.readBytes(serviceArray, 0, serviceLength);
        msg.setService(new String(serviceArray));

        byte resultType = in.readByte();
        msg.setResultType(resultType);

        short targetLen = in.readShort();
        byte[] targetArray = new byte[targetLen];
        in.readBytes(targetArray, 0, targetLen);
        msg.setTargetClass(new String(targetArray));

        short resultLen = in.readShort();
        byte[] resultArray = new byte[resultLen];
        in.readBytes(resultArray, 0, resultLen);

        Object result;
        try {
            result = SerializeUtils.byteArrayToObject(resultArray);
            msg.setResult(result);
            out.add(msg);
        } catch (Exception e) {
            // 跳过这个报文
            skipPacket(in, length);
            logger.error("byte array to obj error:{}", e);
        }
    }

    /**
     * 跳过这个需要解析的报文
     *
     * @param in
     * @param length
     */
    private void skipPacket(ByteBuf in, int length) {
        in.resetReaderIndex();
        in.readerIndex(in.readerIndex() + HEADER_LENGTH + length);
    }
}
