package com.ygf;

import com.ygf.protocol.jessie.api.Service;
import com.ygf.tinyrpc.config.ApplicationConfig;
import com.ygf.tinyrpc.config.ReferenceConfig;
import com.ygf.tinyrpc.config.RegistryConfig;
import com.ygf.tinyrpc.utils.RpcContextUtils;

/**
 * 远程服务调用者--功能测试
 *
 * @author theo
 * @date 20190228
 */
public class Client {
    public static void main(String[] args) throws Exception{
        ReferenceConfig reference = new ReferenceConfig();

        ApplicationConfig application = new ApplicationConfig();
        application.setApplicationName("reference-test");
        reference.setApplication(application);

        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("192.168.1.101:2181");
        registry.setType("zookeeper");
        reference.setRegistry(registry);

        reference.setInterface(Service.class);
        Service ref = (Service)RpcContextUtils.get(reference);
        System.out.println(ref.test());
        Thread.sleep(60*60*1000);
    }
}
