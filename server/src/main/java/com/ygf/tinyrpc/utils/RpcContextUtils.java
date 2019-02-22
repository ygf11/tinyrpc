package com.ygf.tinyrpc.utils;

import com.ygf.tinyrpc.context.RpcContext;
import com.ygf.tinyrpc.context.RpcProvider;

import java.util.List;

/**
 * rpc上下文工具类
 *
 * @author theo
 * @date 20190221
 */
public class RpcContextUtils {
    /**
     * 根据接口，获取具体的代理(因为暴露的服务都是单例的)
     *
     * @param cz
     */
    public static Object getService(Class cz){
        /**
         * 1. 服务url缓存都会通过异步更新
         * 2. 采用随机的方式进行负载均衡
         * 3. 直接从缓存中获取url
         * 4. 如果缓存为空，则尝试同步获取
         */
        RpcContext context = RpcContext.getInstance();
        List<RpcProvider> providers = context.getProviders(cz);

        // 则表示缓存不存在
        if (providers == null){

        }else{

        }


    }
}
