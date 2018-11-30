package com.ygf.tinyrpc.protocol.dubbo.code;

import com.ygf.tinyrpc.protocol.dubbo.message.DubboRequestMessage;
import com.ygf.tinyrpc.serialize.SerializeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 请求消息编码器 把请求消息编码成byte数组
 *
 * @author theo
 * @date 20181128
 */
public class DubboMessageToByteEncoder extends MessageToByteEncoder<DubboRequestMessage> {

    private static Logger logger = LoggerFactory.getLogger(DubboMessageToByteEncoder.class);
    /**
     * service name域的长度占用两个字节
     */
    private static final byte SERVICE_NAME_LENGTH = 2;
    /**
     * 方法参数表中参数个数域长度占用1个字节
     */
    private static final byte PARAMS_SIZE_LENGTH = 1;

    @Override
    protected void encode(ChannelHandlerContext ctx, DubboRequestMessage msg, ByteBuf out) throws Exception {
        if (isInvalid(msg)) {
            logger.info("msg invalid:{}", msg);
            return;
        }

        logger.info("request:{} start encode", msg.getRequestId());
        // dubbo协议
        out.writeByte((byte) 1);
        // 协议版本
        out.writeByte((byte) 0);
        // 请求类型 rpc请求
        out.writeByte((byte) 1);
        // 请求序列号
        out.writeInt(msg.getRequestId());

        // 保存此时的writerIndex
        int saved = out.writerIndex();
        // 占位
        out.writeInt(0);


        String serviceName = msg.getServiceName();
        List<Object> params = msg.getParams();

        int serviceNameLength = serviceName.getBytes().length;
        int paramSize = params.size();

        int length = 0;
        length += SERVICE_NAME_LENGTH;
        length += serviceNameLength + PARAMS_SIZE_LENGTH;

        out.writeShort(serviceNameLength);
        out.writeBytes(serviceName.getBytes());

        for (int i = 0; i < paramSize; ++i) {
            Object param = params.get(i);
            byte[] bytes = SerializeUtils.objectToByteArray(param);
            if (bytes == null) {
                logger.error("serialize failed");
                return;
            }
            length += bytes.length;
            out.writeShort(bytes.length);
            out.writeBytes(bytes, 0, bytes.length);
        }

        // 更新数据段长度
        out.markWriterIndex();
        out.writerIndex(saved);
        out.writeInt(length);
        out.resetWriterIndex();

    }

    /**
     * 判断上游传过来的消息是否有效
     *
     * @param msg
     * @return
     */
    private boolean isInvalid(DubboRequestMessage msg) {
        if (msg == null || msg.getRequestId() == null || StringUtils.isNotBlank(msg.getServiceName())
                 || CollectionUtils.isEmpty(msg.getParams())) {
            return false;
        }

        return true;
    }

}
