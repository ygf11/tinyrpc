package com.ygf.protocol.jessie;

import com.ygf.protocol.jessie.api.Service;
import com.ygf.tinyrpc.common.InitParams;
import com.ygf.tinyrpc.common.RpcMetaData;
import com.ygf.tinyrpc.protocol.jessie.handler.client.RpcOutboundHandler;
import com.ygf.tinyrpc.protocol.jessie.message.InitSessionMessage;
import com.ygf.tinyrpc.protocol.jessie.message.RpcRequestMessage;
import com.ygf.tinyrpc.rpc.OutboundMsg;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

import java.util.ArrayList;
import java.util.List;

/**
 * rpcOutboundHandler测试类
 *
 * @author theo
 * @date 20181211
 */
public class RpcOutboundHandlerTest {
    private RpcOutboundHandler handler;
    private Class service;
    private List<Object> out;
    private Object[] args;
    private Class[] classes;
    private String serviceName = "com.ygf.Service";
    private String appName = "spring-cloud";

    @Before
    public void setup() {
        service = Service.class;
        handler = new RpcOutboundHandler(service);
        classes = new Class[3];
        classes[0] = ChannelHandlerContext.class;
        classes[1] = OutboundMsg.class;
        classes[2] = List.class;
        out = new ArrayList<Object>();
        args = new Object[3];
        args[0] = null;
        args[1] = null;
        args[2] = out;
    }

    /**
     * 会话创建报文请求测试
     *
     * @throws Exception
     */
    @Test
    public void sessionRequest() throws Exception {
        OutboundMsg msg = new OutboundMsg();
        msg.setType(CREATE_SESSION_REQUEST);
        InitParams params = new InitParams(serviceName, appName);
        msg.setArg(params);
        args[1] = msg;
        MethodUtils.invokeMethod(handler, true, "encode", args, classes);

        InitSessionMessage message = (InitSessionMessage) out.get(0);
        Assert.assertEquals(PROTOCOL, message.getProtocol());
        Assert.assertEquals(CURRENT_VERSION, (long) message.getVersion());
        Assert.assertEquals(CREATE_SESSION_REQUEST, message.getType());
        Assert.assertEquals(0, message.getSessionId());
        Assert.assertEquals(appName, message.getAppName());
        Assert.assertEquals(serviceName, message.getService());
    }

    /**
     * rpc请求报文处理测试
     */
    @Test
    public void rpcRequest() throws Exception {
        OutboundMsg msg = new OutboundMsg();
        RpcMetaData metaData = new RpcMetaData();
        metaData.setSessionId(123);
        metaData.setRequestId(500);
        metaData.setService("com.ygf.Service");
        metaData.setMethod("test");
        List<String> paramTypes = new ArrayList<String>();
        paramTypes.add("java.lang.Integer");
        paramTypes.add("java.lang.String");
        paramTypes.add("java.lang.String");
        metaData.setParamTypes(paramTypes);
        List<Object> params = new ArrayList<Object>();
        params.add(1);
        params.add("2");
        params.add("3");
        metaData.setArgs(params);
        msg.setType(RPC_REQUEST);
        msg.setArg(metaData);
        args[1] = msg;
        MethodUtils.invokeMethod(handler, true, "encode", args, classes);

        RpcRequestMessage message = (RpcRequestMessage) out.get(0);
        Assert.assertEquals(PROTOCOL, message.getProtocol());
        Assert.assertEquals(CURRENT_VERSION, (long) message.getVersion());
        Assert.assertEquals(RPC_REQUEST, message.getType());
        Assert.assertEquals(123, message.getSessionId());
        Assert.assertEquals(500, message.getRequestId());
        Assert.assertEquals("com.ygf.Service:test", message.getMethod());
        Assert.assertEquals(3, message.getParams().size());
        Assert.assertEquals(Integer.class.getCanonicalName(), message.getParamTypes().get(0));
        Assert.assertEquals(String.class.getCanonicalName(), message.getParamTypes().get(1));
        Assert.assertEquals(String.class.getCanonicalName(), message.getParamTypes().get(2));
    }
}
