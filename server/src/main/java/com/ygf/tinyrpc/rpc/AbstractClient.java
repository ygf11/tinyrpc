package com.ygf.tinyrpc.rpc;

import com.ygf.tinyrpc.protocol.jessie.message.Header;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * client的抽象类， 主要提供channel的注册和访问
 *
 * @author theo
 * @date 20181211
 */
public class AbstractClient {
    /**
     * 服务到通信channel的映射
     */
    private final Map<Class, Channel> channelMap = new ConcurrentHashMap<Class, Channel>();

    /**
     * 注册channel
     * TODO 更新channel时的保护操作
     * @param service
     * @param channel
     */
    public void resgiterChannel(Class service, Channel channel) {
        channelMap.put(service, channel);
    }

    /**
     * 两种方式向channel写入消息：
     * 1. 当前线程在channel注册的eventloop中时，直接在这个线程中执行代码
     * 2. 当前线程不在channel注册的evetloop时， 提交到这个线程中稍候执行
     *
     * TODO 改成抽象类方法 与childhandler共用这个方法
     * @param msg
     */
    protected void writeMsg(Class service, final OutboundMsg msg) {
        final  Channel channel = channelMap.get(service);
        if (channel.eventLoop().inEventLoop()) {
            channel.write(msg);
        } else {
            channel.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    channel.write(msg);
                }
            });
        }
    }

}
