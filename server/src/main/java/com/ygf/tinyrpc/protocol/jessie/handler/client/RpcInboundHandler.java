package com.ygf.tinyrpc.protocol.jessie.handler.client;

import com.ygf.tinyrpc.common.RpcResult;
import com.ygf.tinyrpc.protocol.jessie.message.Header;
import com.ygf.tinyrpc.protocol.jessie.message.RpcResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.ygf.tinyrpc.rpc.client.RpcClient;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端处理入站事件的处理器
 * TODO 异常处理
 *
 * @author theo
 * @date 20181208
 */
public class RpcInboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RpcInboundHandler.class);

    /**
     * 连接对应的服务
     */
    private Class service;
    /**
     * rpcClient
     */
    private RpcClient rpcClient;
    public RpcInboundHandler(Class service, RpcClient rpcClient){
        super();
        this.service = service;
        this.rpcClient = rpcClient;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
        boolean isHeader = message instanceof Header;
        if (!isHeader) {
            logger.warn("msg {] is not a jessie header", message);
            return;
        }

        Header msg = (Header) message;
        switch (msg.getType()) {
            case RPC_RESPONSE:
                rpcResponse(msg);
                break;
            case CREATE_SESSION_RESPONSE:
                sessionResponse(msg);
                break;
            case HEARTBEATS:
                break;
            default:
                logger.error("jessie not supported outbound type {}", msg.getType());
        }
    }

    /**
     * 解析session响应
     *
     * @param msg
     */
    private void sessionResponse(Header msg) {
        // TODO 启动心跳线程
        assert msg.getSessionId() != 0;
        rpcClient.handleSessionInit(service, msg.getSessionId());
    }

    /**
     * 解析rpc响应
     *
     * @param header
     */
    private void rpcResponse(Header header) {
        boolean isResponse = header instanceof RpcResponseMessage;
        if (!isResponse) {
            logger.warn("msg {] is not a rpc response msg", header);
            return;
        }

        RpcResponseMessage msg = (RpcResponseMessage) header;
        int requestId = msg.getRequestId();
        RpcResult result = new RpcResult();
        result.setType(msg.getResultType());
        result.setResultType(msg.getTargetClass());
        result.setResult(msg.getResult());

        //session.putResult(requestId, result);

        // TODO 更新心跳
        // TODO 提交任务
    }




}
