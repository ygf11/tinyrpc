package com.ygf.tinyrpc.protocol.jessie.handler;

import com.ygf.tinyrpc.protocol.jessie.message.Header;
import io.netty.channel.Channel;

/**
 * RpcHandler和ChildHandler的公共父类，有一个writeMsg()到的公共方法
 *
 * @author theo
 * @date 20181207
 */
public abstract class AbstractHandler {

    protected Channel channel;
    public AbstractHandler(Channel channel){
        this.channel = channel;
    }

    /**
     * 两种方式向channel写入消息：
     * 1. 当前线程在channel注册的eventloop中时，直接在这个线程中执行代码
     * 2. 当前线程不在channel注册的evetloop时， 提交到这个线程中稍候执行
     *
     * TODO 改成抽象类方法 与childhandler共用这个方法
     * @param msg
     */
    protected void writeMsg(final Header msg) {
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
