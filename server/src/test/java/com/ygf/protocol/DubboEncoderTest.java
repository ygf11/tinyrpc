package com.ygf.protocol;

import com.ygf.tinyrpc.common.IdGenertor;
import com.ygf.tinyrpc.protocol.dubbo.code.DubboMessageToByteEncoder;
import com.ygf.tinyrpc.protocol.dubbo.message.DubboRequestMessage;
import com.ygf.tinyrpc.serialize.SerializeUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class DubboEncoderTest {

    private static Logger logger = LoggerFactory.getLogger(DubboEncoderTest.class);

    private DubboMessageToByteEncoder encoder;
    private DubboRequestMessage msg;
    private ByteBuf byteBuf;
    private List<Object> params;


    @Before
    public void setup() {
        buildMessage();
        byteBuf = UnpooledByteBufAllocator.DEFAULT.ioBuffer();
    }

    private void buildMessage() {
        msg = new DubboRequestMessage();
        msg.setRequestId(IdGenertor.incrementAndGet());
        msg.setServiceName("com.ygf.protocol.DubboEncoder.test()");
        List<String> paramNames = new ArrayList<String>();
        params = new ArrayList<Object>();
        params.add((Integer) 1);
        params.add((Integer) 2);
        params.add((Integer) 3);
        msg.setParams(params);
    }


    /**
     * test
     */
    @Test
    public void mytest() throws Exception {
        DubboMessageToByteEncoder encoder = new DubboMessageToByteEncoder();

        // 参数列表
        Class[] classes = new Class[3];
        classes[0] = ChannelHandlerContext.class;
        classes[1] = DubboRequestMessage.class;
        classes[2] = ByteBuf.class;
        // 参数
        Object[] args = new Object[3];
        args[0] = null;
        args[1] = msg;
        args[2] = byteBuf;
        MethodUtils.invokeMethod(encoder, true, "encode", args, classes);
        logger.info("result:{}", byteBuf);
        byteBuf.readerIndex(0);
        byte protocol = byteBuf.readByte();
        byte type = byteBuf.readByte();
        byte version = byteBuf.readByte();
        int requestId = byteBuf.readInt();
        int length = byteBuf.readInt();
        // 服务名
        short serviceStringlength = byteBuf.readShort();
        byte[] seviceName = new byte[serviceStringlength];
        byteBuf.readBytes(seviceName, 0, serviceStringlength);

        Assert.assertEquals(Byte.valueOf("1").byteValue(), protocol);
        Assert.assertEquals(Byte.valueOf("1").byteValue(), type);
        Assert.assertEquals(Byte.valueOf("0").byteValue(), version);
        Assert.assertEquals(Integer.valueOf("1").intValue(), requestId);
        Assert.assertEquals("com.ygf.protocol.DubboEncoder.test()".length(), serviceStringlength);
        Assert.assertEquals("com.ygf.protocol.DubboEncoder.test()", new String(seviceName));

        for (int i = 0; i < 3; ++i){
            short l = byteBuf.readShort();
            byte[] param = new byte[l];
            byteBuf.readBytes(param, 0, l);
            Assert.assertEquals(params.get(i), SerializeUtils.byteArrayToObject(param));
        }
    }
}