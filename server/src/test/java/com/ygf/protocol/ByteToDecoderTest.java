package com.ygf.protocol;

import com.ygf.tinyrpc.common.IdGenertor;
import com.ygf.tinyrpc.protocol.jessie.code.ByteToMsgDecoder;
import com.ygf.tinyrpc.protocol.jessie.code.MsgToByteEncoder;
import com.ygf.tinyrpc.protocol.jessie.message.Header;
import com.ygf.tinyrpc.protocol.jessie.message.RpcRequestMessage;
import com.ygf.tinyrpc.protocol.jessie.message.RpcResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

import static com.ygf.tinyrpc.protocol.jessie.message.DubboProtocol.*;

import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 解码器测试类
 *
 * @author theo
 * @date 20181201
 */
public class ByteToDecoderTest {

    private static Logger logger = LoggerFactory.getLogger(ByteToDecoderTest.class);

    private Header header;
    private ByteToMsgDecoder decoder;
    private MsgToByteEncoder encoder;
    private ByteBuf in;
    private Class[] classes;
    private Object[] args;
    private List<Object> out;

    @Before
    public void setup() {
        decoder = new ByteToMsgDecoder();
        encoder = new MsgToByteEncoder();
        in = UnpooledByteBufAllocator.DEFAULT.ioBuffer(1024);
        header = new Header();
        header.setProtocol(PROTOCOL);
        header.setVersion((byte) 0);
        header.setSessionId(0);

        out = new ArrayList<Object>();

        // 参数列表
        classes = new Class[3];
        classes[0] = ChannelHandlerContext.class;
        classes[1] = ByteBuf.class;
        classes[2] = List.class;
        // 参数
        args = new Object[3];
        args[0] = null;
        args[1] = in;
        args[2] = out;
    }

    /**
     * 反射调用encode方法写入数据
     *
     * @param in
     * @param msg
     * @throws Exception
     */
    private void writePacketData(ByteBuf in, Header msg) throws Exception {
        Class[] classes = new Class[3];
        // 参数列表
        classes[0] = ChannelHandlerContext.class;
        classes[1] = Header.class;
        classes[2] = ByteBuf.class;
        // 参数
        Object[] args = new Object[3];
        args[0] = null;
        args[1] = msg;
        args[2] = in;
        MethodUtils.invokeMethod(encoder, true, "encode", args, classes);
    }

    /**
     * 非rpc相关报文解码
     */
    @Test
    public void notRpcPacketTest() throws Exception {
        for (byte i = 1; i < 8; ++i) {
            if (i == RPC_REQUEST || i == RPC_RESPONSE) {
                continue;
            }
            in.clear();
            out.clear();
            header.setType(i);
            writePacketData(in, header);

            MethodUtils.invokeMethod(decoder, true, "decode", args, classes);
            Header result = (Header) out.get(0);
            Assert.assertEquals(header.getProtocol(), result.getProtocol());
            Assert.assertEquals(header.getVersion(), result.getVersion());
            Assert.assertEquals(header.getType(), result.getType());
            Assert.assertEquals(header.getSessionId(), result.getSessionId());
            Assert.assertFalse(result instanceof RpcRequestMessage);
            Assert.assertFalse(result instanceof RpcResponseMessage);
        }
    }

    /**
     * rpc请求报文解码 正常解析
     */
    @Test
    public void RpcRequestTest() throws Exception {
        header.setType(RPC_REQUEST);
        RpcRequestMessage msg = new RpcRequestMessage(header);
        int requestId = IdGenertor.incrementAndGet();
        msg.setRequestId(requestId);
        msg.setService("com.ygf.protocol.DubboEncoder.test()");
        List<Object> params = new ArrayList<Object>();
        params.add((Integer) 4);
        params.add((Integer) 5);
        params.add((Integer) 6);
        msg.setParams(params);

        writePacketData(in, msg);

        MethodUtils.invokeMethod(decoder, true, "decode", args, classes);
        RpcRequestMessage result = (RpcRequestMessage) out.get(0);
        Assert.assertEquals(msg.getProtocol(), result.getProtocol());
        Assert.assertEquals(msg.getVersion(), result.getVersion());
        Assert.assertEquals(msg.getType(), result.getType());
        Assert.assertEquals(msg.getSessionId(), result.getSessionId());
        Assert.assertEquals(msg.getRequestId(), result.getRequestId());
        Assert.assertEquals(msg.getParams().size(), result.getParams().size());
        Assert.assertEquals(msg.getService(), msg.getService());
        for (int i = 0; i < msg.getParams().size(); ++i) {
            Assert.assertEquals(msg.getParams().get(i), result.getParams().get(i));
        }
    }

    /**
     * rpc响应报文解析 正常解析
     */
    @Test
    public void RpcResponseTest() throws Exception {
        header.setType(RPC_RESPONSE);
        RpcResponseMessage msg = new RpcResponseMessage(header);
        int requestId = IdGenertor.incrementAndGet();
        msg.setRequestId(requestId);
        msg.setService("com.ygf.protocol.DubboEncoder.test()");
        msg.setResultType((byte) 1);
        msg.setTargetClass("java.lang.Integer");
        msg.setResult(Integer.valueOf("110"));

        writePacketData(in, msg);

        MethodUtils.invokeMethod(decoder, true, "decode", args, classes);
        RpcResponseMessage result = (RpcResponseMessage) out.get(0);
        Assert.assertEquals(msg.getProtocol(), result.getProtocol());
        Assert.assertEquals(msg.getVersion(), result.getVersion());
        Assert.assertEquals(msg.getType(), result.getType());
        Assert.assertEquals(msg.getSessionId(), result.getSessionId());
        Assert.assertEquals(msg.getRequestId(), result.getRequestId());
        Assert.assertEquals(msg.getService(), result.getService());
        Assert.assertEquals(msg.getResultType(), result.getResultType());
        Assert.assertEquals(msg.getTargetClass(), result.getTargetClass());
        Assert.assertEquals(msg.getResult(), result.getResult());
    }

    /**
     * rpc请求报文解析异常
     */
    @Test
    public void RpcRequestFailTest() throws Exception {
        header.setType(RPC_REQUEST);
        RpcRequestMessage msg = new RpcRequestMessage(header);
        int requestId = IdGenertor.incrementAndGet();
        msg.setRequestId(requestId);
        msg.setService("com.ygf.protocol.DubboEncoder.test()");
        List<Object> params = new ArrayList<Object>();
        params.add((Integer) 4);
        params.add((Integer) 5);
        params.add((Integer) 6);
        msg.setParams(params);

        writePacketData(in, msg);
        // 修改参数1 使之不能正常反序列化
        in.markWriterIndex();
        byte[] bytes = new byte[81];
        in.writerIndex(56);
        in.writeBytes(bytes, 0, 81);

        in.resetWriterIndex();
        MethodUtils.invokeMethod(decoder, true, "decode", args, classes);
    }

    /**
     * rpc响应报文解析异常
     */
    @Test
    public void RpcResponseFailTest() throws Exception {
        header.setType(RPC_RESPONSE);
        RpcResponseMessage msg = new RpcResponseMessage(header);
        int requestId = IdGenertor.incrementAndGet();
        msg.setRequestId(requestId);
        msg.setService("com.ygf.protocol.DubboEncoder.test()");
        msg.setResultType((byte) 1);
        msg.setTargetClass("java.lang.Integer");
        msg.setResult(Integer.valueOf("110"));

        writePacketData(in, msg);

        in.markWriterIndex();
        in.writerIndex(75);
        byte[] bytes = new byte[81];
        in.writeBytes(bytes, 0, 81);
        in.resetWriterIndex();

        MethodUtils.invokeMethod(decoder, true, "decode", args, classes);
    }

}
