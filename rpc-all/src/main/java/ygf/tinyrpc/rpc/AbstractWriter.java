package ygf.tinyrpc.rpc;

import ygf.tinyrpc.protocol.jessie.common.Session;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * sender抽象类， 主要提供channel的注册和写入
 *
 * @author theo
 * @date 20181211
 */
public class AbstractWriter {
    /**
     * 服务到通信channel的映射
     */
    private final Map<Session, Channel> channelMap = new ConcurrentHashMap<Session, Channel>();

    /**
     * 注册channel
     * TODO 更新channel时的保护操作
     * @param session
     * @param channel
     */
    public void registerChannel(Session session, Channel channel) {
        channelMap.put(session, channel);
    }

    /**
     * 两种方式向channel写入消息：
     * 1. 当前线程在channel注册的eventloop中时，直接在这个线程中执行代码
     * 2. 当前线程不在channel注册的evetloop时， 提交到这个线程中稍候执行
     *
     * TODO 改成抽象类方法 与childhandler共用这个方法
     * @param session
     */
    protected void writeMsg(Session session, final OutboundMsg msg) {
        final  Channel channel = channelMap.get(session);
        if (channel.eventLoop().inEventLoop()) {
            channel.writeAndFlush(msg);
        } else {
            channel.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    channel.writeAndFlush(msg);
                }
            });
        }
    }

}
