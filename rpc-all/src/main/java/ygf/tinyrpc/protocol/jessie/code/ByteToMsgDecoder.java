package ygf.tinyrpc.protocol.jessie.code;

import ygf.tinyrpc.protocol.jessie.message.InitSessionMessage;
import ygf.tinyrpc.protocol.jessie.message.Header;
import ygf.tinyrpc.protocol.jessie.message.RpcRequestMessage;
import ygf.tinyrpc.protocol.jessie.message.RpcResponseMessage;

import static ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

import ygf.tinyrpc.serialize.SerializeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * 字节流读取的解码器
 * TODO 异常处理 客户端和服务器端编码解码代码分离
 *
 * @author theo
 * @date 20181130
 */
public class ByteToMsgDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(ByteToMsgDecoder.class);
    private static final byte DUBBO_PROTOCOL = 1;


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        logger.info("buf: {}", in);
        // 报文数据不够
        if (in.readableBytes() < HEADER_LENGTH) {
            return;
        }
        // 保留位置 以便回滚
        in.markReaderIndex();

        byte protocol = in.readByte();

        // 如果不是jessie的数据报文  则不进行解析
        if (protocol != PROTOCOL) {
            in.readerIndex(0);
            return;
        }

        Header header = new Header();
        header.setProtocol(PROTOCOL);
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
                parseInitSessionPacket(in, header, out);
                break;
            case CREATE_SESSION_RESPONSE:
                //case CREATE_SESSION_ACK:
            case EXIT_SESSION:
            case HEARTBEATS:
                parseGeneralPacket(in, header, out);
                break;
            default:
                logger.error("jessie not support type {}", header.getType());
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
     * 解析创建会话的报文
     *
     * @param in
     * @param header
     * @param out
     */
    private void parseInitSessionPacket(ByteBuf in, Header header, List<Object> out) {
        int length = in.readInt();
        // 报文数据不够
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        String service = readString(in);
        String appName = readString(in);
        InitSessionMessage msg = new InitSessionMessage(header);
        msg.setService(service);
        msg.setAppName(appName);

        out.add(msg);
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
        msg.setMethod(readString(in));

        int count = in.readByte();
        // 解析参数值
        try {
            msg.setParamTypes(readList(in, count));
            msg.setParams(readList(in, count));
        } catch (Exception e) {
            skipPacket(in, length);
            return;
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

        // requestId
        msg.setRequestId(in.readInt());
        // resultClass
        msg.setResultClass(readString(in));

        // result
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

    /**
     * 从二进制报文中读取String, 格式为len+String
     *
     * @param in
     * @return
     */
    private String readString(ByteBuf in) {
        short len = in.readShort();
        byte[] array = new byte[len];
        in.readBytes(array, 0, len);
        return new String(array);
    }

    /**
     * 读取一个列表
     *
     * @param in
     * @return
     * @throws Exception
     */
    private List readList(ByteBuf in, int count) throws Exception {
        List list = new ArrayList();

        for (byte i = 0; i < count; ++i) {
            short len = in.readShort();
            byte[] bytes = new byte[len];
            in.readBytes(bytes, 0, len);
            Object param = null;
            param = SerializeUtils.byteArrayToObject(bytes);
            list.add(param);
        }

        return list;
    }

}
