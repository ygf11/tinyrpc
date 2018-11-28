package com.ygf.tinyrpc.protocol.dubbo.code;

import com.ygf.tinyrpc.protocol.dubbo.message.DubboRequestMessage;
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
     * service name字段长度占用两个字节
     */
    private static final byte SERVICE_LENGTH = 2;
    /**
     * 方法参数表中参数个数占用1个字节
     */
    private static final byte PARAMS_SIZE = 1;

    @Override
    protected void encode(ChannelHandlerContext ctx, DubboRequestMessage msg, ByteBuf out) throws Exception {
        if (isInvalid(msg)) {
            logger.info("msg invalid:{}", msg);
            return;
        }
        int requestId = msg.getRequestId();
        //byte protocol = msg.getProtocol();
        //byte version = msg.getVersion();
        //byte type = msg.getType();
        String serviceName = msg.getServiceName();
        List<Object> params = msg.getParams();
        // 2字节
        int serviceNameLength = serviceName.getBytes().length;
        // serviceName

        // 2字节
        int paramSize = params.size();


        int lenth = 0;
        lenth = SERVICE_LENGTH + serviceName.getBytes().length;
        lenth += PARAMS_SIZE;
        for (int i = 0; i < paramSize; ++i) {

        }
    }

    /**
     * 判断上游传过来的消息是否有效
     *
     * @param msg
     * @return
     */
    private boolean isInvalid(DubboRequestMessage msg) {
        if (msg == null || msg.getRequestId() == null || StringUtils.isNotBlank(msg.getServiceName())
                || CollectionUtils.isEmpty(msg.getParamNames()) || CollectionUtils.isEmpty(msg.getParams())) {
            return false;
        }

        return true;
    }

}
