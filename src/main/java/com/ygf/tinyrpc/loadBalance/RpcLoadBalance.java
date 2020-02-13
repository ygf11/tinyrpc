package com.ygf.tinyrpc.loadBalance;

import com.ygf.tinyrpc.context.RpcProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 负载均衡实现类
 *
 * @author theo
 * @date 20190228
 */
public class RpcLoadBalance {
    private Logger logger = LoggerFactory.getLogger(RpcLoadBalance.class);

    public RpcLoadBalance(){}

    /**
     * 从里面随机获取一个
     *
     * @param providers
     * @return
     */
    public RpcProvider getFromProviders(List<RpcProvider> providers){
        return providers.get(0);
    }
}
