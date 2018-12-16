package com.ygf.tinyrpc.protocol.jessie.handler.server;

import com.ygf.tinyrpc.common.RpcMetaData;
import com.ygf.tinyrpc.protocol.jessie.handler.AbstractRpcInboundHandler;
import com.ygf.tinyrpc.protocol.jessie.message.Header;
import com.ygf.tinyrpc.protocol.jessie.message.InitSessionMessage;
import com.ygf.tinyrpc.protocol.jessie.message.RpcRequestMessage;
import com.ygf.tinyrpc.rpc.server.RpcChildServer;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ygf.tinyrpc.protocol.jessie.message.JessieProtocol.*;

/**
 * provider端处理入站消息(读消息)的处理器
 * // 异常处理
 *
 * @author theo
 * @date 20181213
 */
public class RpcChildInboundHandler extends AbstractRpcInboundHandler {

    private static Logger logger = LoggerFactory.getLogger(RpcChildInboundHandler.class);
    /**
     * rpcChildServer
     */
    private RpcChildServer server;

    public RpcChildInboundHandler(RpcChildServer server) {
        super();
        this.server = server;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean isHeader = msg instanceof Header;
        if (!isHeader) {
            logger.warn("message {] is not a jessie header", msg);
            return;
        }

        Header header = (Header) msg;
        byte type = header.getType();
        String addr = getServerAddr(ctx.channel());
        switch (type) {
            case RPC_REQUEST:
                handlerRpcRequest(header, addr);
                break;
            case EXIT_SESSION:
                break;
            case HEARTBEATS:
                break;
            case CREATE_SESSION_REQUEST:
                handleSessionInit(header, addr);
                break;
            default:
                logger.error("not supported request type:{}", type);
        }
    }

    /**
     * 处理创建会话的请求
     *
     * @param header
     * @param addr
     */
    private void handleSessionInit(Header header, String addr) throws Exception{
        boolean isInitSession = header instanceof InitSessionMessage;
        if (!isInitSession) {
            logger.warn("msg is not a session init msg");
            return;
        }

        InitSessionMessage msg = (InitSessionMessage) header;
        server.handleSessionInit(addr, msg.getService(), msg.getAppName());

        // TODO更新心跳
    }

    /**
     * 处理rpc请求
     *
     * @param header
     * @param addr
     */
    private void handlerRpcRequest(Header header, String addr) {
        boolean isRpcRequest = header instanceof RpcRequestMessage;
        if (!isRpcRequest) {
            logger.warn("msg is not a rpc request msg");
            return;
        }

        RpcRequestMessage msg = (RpcRequestMessage) header;
        RpcMetaData metaData = new RpcMetaData();
        metaData.setSessionId(msg.getSessionId());
        metaData.setRequestId(msg.getRequestId());
        metaData.setService(msg.getMethod());

        // 设置服务名+方法
        String[] service= msg.getMethod().split(":");
        // TODO 异常处理
        if (service.length != 2){
            logger.error("class:method:{} error", msg.getMethod());
            return;
        }
        metaData.setService(service[0]);
        metaData.setService(service[1]);

        metaData.setParamTypes(msg.getParamTypes());
        metaData.setArgs(msg.getParams());

    }


}
