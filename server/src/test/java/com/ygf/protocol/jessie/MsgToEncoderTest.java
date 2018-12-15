package com.ygf.protocol.jessie;

import com.ygf.tinyrpc.protocol.jessie.code.MsgToByteEncoder;
import com.ygf.tinyrpc.protocol.jessie.message.InitSessionMessage;
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
    private String service = "com.ygf.Service";
    private String method = "com.ygf.Service:test";

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
     * 无数据段报文验证 type=2,3,4,7
     */
    @Test
    public void notRpcRequest() throws Exception {
        for (byte i = 1; i < 8; ++i) {
            if (i == RPC_REQUEST || i == RPC_RESPONSE || i == CREATE_SESSION_REQUEST) {
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
     * 创建会话的请求报文验证
     */
    @Test
    public void createSessionTest() throws Exception{
        InitSessionMessage msg = new InitSessionMessage(header);
        msg.setSessionId(0);
        msg.setProtocol(PROTOCOL);
        msg.setVersion(CURRENT_VERSION);
        msg.setType(CREATE_SESSION_REQUEST);
        msg.setAppName("panama-cloud-application");
        msg.setService(service);
        args[1] = msg;
        MethodUtils.invokeMethod(encoder, true, "encode", args, classes);
        Assert.assertEquals(PROTOCOL, byteBuf.readByte());
        Assert.assertEquals(CURRENT_VERSION, byteBuf.readByte());
        Assert.assertEquals(CREATE_SESSION_REQUEST, byteBuf.readByte());
        // sessionId
        Assert.assertEquals(0, byteBuf.readInt());
        // requestId
        int length = byteBuf.readInt();
        // service
        byte[] bytes = new byte[byteBuf.readShort()];
        byteBuf.readBytes(bytes, 0, bytes.length);
        Assert.assertEquals(service, new String(bytes));
        // appName
        bytes = new byte[byteBuf.readShort()];
        byteBuf.readBytes(bytes, 0, bytes.length);
        Assert.assertEquals("panama-cloud-application", new String(bytes));
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
        request.setRequestId(1);
        request.setMethod(method);

        List<String> paramTypes = new ArrayList<String>();
        paramTypes.add(Integer.class.getCanonicalName());
        paramTypes.add(Integer.class.getCanonicalName());
        paramTypes.add(Integer.class.getCanonicalName());
        request.setParamTypes(paramTypes);

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
        Assert.assertEquals(method.length(), serviceLength);
        Assert.assertEquals(method, new String(bytes));

        // 参数类型验证
        Assert.assertEquals(3, byteBuf.readByte());
        for (int i = 0; i < 3; ++i){
            short len = byteBuf.readShort();
            byte[] array = new byte[len];
            byteBuf.readBytes(array, 0, len);
            Assert.assertEquals(Integer.class.getCanonicalName(),
                    SerializeUtils.byteArrayToObject(array));
        }

        // 参数验证
        //Assert.assertEquals(3, byteBuf.readByte());
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
        response.setSessionId(123);
        int requestId = 1;
        response.setRequestId(requestId);
        response.setResultType((byte) 1);
        response.setResultClass("java.lang.Integer");
        response.setResult((Integer.valueOf("1")));

        args[1] = response;

        MethodUtils.invokeMethod(encoder, true, "encode", args, classes);
        Assert.assertEquals(PROTOCOL, byteBuf.readByte());
        Assert.assertEquals((byte) 1, byteBuf.readByte());
        Assert.assertEquals(RPC_RESPONSE, byteBuf.readByte());
        Assert.assertEquals(123, byteBuf.readInt());

        int length = byteBuf.readInt();
        // requestid
        Assert.assertEquals(requestId, byteBuf.readInt());
        // resultType
        Assert.assertEquals(1, byteBuf.readByte());

        // resultClass
        int resultLen = byteBuf.readShort();
        byte[] bytes = new byte[resultLen];
        byteBuf.readBytes(bytes, 0, resultLen);

        Assert.assertEquals("java.lang.Integer".length(), resultLen);
        Assert.assertEquals("java.lang.Integer", new String(bytes));

        // result
        int len = byteBuf.readShort();
        byte[] array = new byte[len];
        byteBuf.readBytes(array, 0, len);
        Assert.assertEquals(Integer.valueOf("1"), SerializeUtils.byteArrayToObject(array));
    }
}
