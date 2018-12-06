package com.ygf.protocol.jessie;

import com.ygf.tinyrpc.common.IdGenertor;
import com.ygf.tinyrpc.protocol.jessie.code.MsgToByteEncoder;
import com.ygf.tinyrpc.protocol.jessie.message.Header;
import com.ygf.tinyrpc.protocol.jessie.message.RpcRequestMessage;
import com.ygf.tinyrpc.protocol.jessie.message.RpcResponseMessage;
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

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

/**
 * session和heartbeat相关的报文编码测试类
 *
 * @author theo
 * @date 20181130
 */
public class MsgToEncoderTest {
    private static Logger logger = LoggerFactory.getLogger(MsgToEncoderTest.class);

    private Header header;
    private MsgToByteEncoder encoder;
    private ByteBuf byteBuf;
    private Class[] classes;
    private Object[] args;

    @Before
    public void setup() {
        byteBuf = UnpooledByteBufAllocator.DEFAULT.ioBuffer();
        encoder = new MsgToByteEncoder();
        header = new Header();
        header.setProtocol(PROTOCOL);
        header.setSessionId(0);
        header.setVersion((byte) 0);
        // 参数列表
        classes = new Class[3];
        classes[0] = ChannelHandlerContext.class;
        classes[1] = Header.class;
        classes[2] = ByteBuf.class;
        // 参数
        args = new Object[3];
        args[0] = null;
        args[1] = header;
        args[2] = byteBuf;
    }

    /**
     * 无数据段报文验证 type=1,2,3,4,7
     */
    @Test
    public void notRpcRequest() throws Exception {
        for (byte i = 1; i < 8; ++i) {
            if (i == RPC_REQUEST || i == RPC_RESPONSE) {
                continue;
            }
            header.setType(i);
            MethodUtils.invokeMethod(encoder, true, "encode", args, classes);

            Assert.assertEquals(PROTOCOL, byteBuf.readByte());
            Assert.assertEquals((byte) 0, byteBuf.readByte());
            Assert.assertEquals(i, byteBuf.readByte());
            Assert.assertEquals(0, byteBuf.readInt());
            Assert.assertEquals(0, byteBuf.readInt());
        }
    }

    /**
     * rpc请求报文 type=5
     */
    @Test
    public void rpcRequestTest() throws Exception {
        RpcRequestMessage request = new RpcRequestMessage();
        request.setProtocol(PROTOCOL);
        request.setVersion((byte) 1);
        request.setSessionId(0);
        request.setType(RPC_REQUEST);
        request.setRequestId(IdGenertor.incrementAndGet());
        request.setService("com.ygf.protocol.DubboEncoder.test()");
        List<Object> params = new ArrayList<Object>();
        params.add((Integer) 4);
        params.add((Integer) 5);
        params.add((Integer) 6);
        request.setParams(params);

        args[1] = request;

        MethodUtils.invokeMethod(encoder, true, "encode", args, classes);
        Assert.assertEquals(PROTOCOL, byteBuf.readByte());
        Assert.assertEquals((byte) 1, byteBuf.readByte());
        Assert.assertEquals(RPC_REQUEST, byteBuf.readByte());
        Assert.assertEquals(0, byteBuf.readInt());
        byteBuf.readInt();
        int length = byteBuf.readInt();
        int serviceLength = byteBuf.readShort();
        byte[] bytes = new byte[serviceLength];
        byteBuf.readBytes(bytes, 0, serviceLength);
        Assert.assertEquals("com.ygf.protocol.DubboEncoder.test()".length(), serviceLength);
        Assert.assertEquals("com.ygf.protocol.DubboEncoder.test()", new String(bytes));

        Assert.assertEquals(3, byteBuf.readByte());

        for (int i = 4; i < 7; ++i) {
            short len = byteBuf.readShort();
            byte[] array = new byte[len];
            byteBuf.readBytes(array, 0, len);
            Assert.assertEquals(i, SerializeUtils.byteArrayToObject(array));
        }
    }

    /**
     * rpc响应报文 type=6
     */
    @Test
    public void rpcResponseTest() throws Exception {
        RpcResponseMessage response = new RpcResponseMessage();
        response.setProtocol(PROTOCOL);
        response.setVersion((byte) 1);
        response.setType(RPC_RESPONSE);
        response.setSessionId(0);
        int requestId = IdGenertor.incrementAndGet();
        response.setRequestId(requestId);
        response.setService("com.ygf.protocol.DubboEncoder.test()");
        response.setResultType((byte) 1);
        response.setTargetClass("java.lang.Integer");
        response.setResult((Integer.valueOf("1")));

        args[1] = response;

        MethodUtils.invokeMethod(encoder, true, "encode", args, classes);
        Assert.assertEquals(PROTOCOL, byteBuf.readByte());
        Assert.assertEquals((byte) 1, byteBuf.readByte());
        Assert.assertEquals(RPC_RESPONSE, byteBuf.readByte());
        Assert.assertEquals(0, byteBuf.readInt());

        int length = byteBuf.readInt();

        Assert.assertEquals(requestId, byteBuf.readInt());
        int serviceLength = byteBuf.readShort();
        byte[] bytes = new byte[serviceLength];
        byteBuf.readBytes(bytes, 0, serviceLength);
        Assert.assertEquals("com.ygf.protocol.DubboEncoder.test()".length(), serviceLength);
        Assert.assertEquals("com.ygf.protocol.DubboEncoder.test()", new String(bytes));
        Assert.assertEquals(1, byteBuf.readByte());


        int resultLen = byteBuf.readShort();
        bytes = new byte[resultLen];
        byteBuf.readBytes(bytes, 0, resultLen);

        Assert.assertEquals("java.lang.Integer".length(), resultLen);
        Assert.assertEquals("java.lang.Integer", new String(bytes));

        int len = byteBuf.readShort();
        byte[] array = new byte[len];
        byteBuf.readBytes(array, 0, len);
        Assert.assertEquals(Integer.valueOf("1"), SerializeUtils.byteArrayToObject(array));
    }
}