package com.ygf.example.server;

import com.ygf.example.api.Service;
import com.ygf.example.service.ServiceImpl;
import com.ygf.tinyrpc.config.ApplicationConfig;
import com.ygf.tinyrpc.config.ProtocolConfig;
import com.ygf.tinyrpc.config.RegistryConfig;
import com.ygf.tinyrpc.config.ServiceConfig;
import com.ygf.tinyrpc.utils.RpcContextUtils;

public class Server {
    public  static void main(String[] args) throws Exception{
        Service testService = new ServiceImpl();
        ServiceConfig service = new ServiceConfig();

        ApplicationConfig application = new ApplicationConfig();
        application.setApplicationName("test");
        service.setApplication(application);

        RegistryConfig registry = new RegistryConfig();
        registry.setType("zookeeper");
        registry.setAddress("192.168.43.98:2181");

        service.setRegistry(registry);

        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("jessie");
        protocol.setHost("192.168.43.98");
        protocol.setPort(20880);
        service.setProtocol(protocol);

        service.setInterface(Service.class);
        service.setRef(testService);
        // 暴露服务
        RpcContextUtils.export(service);
        // 等待远程调用
        Thread.sleep(60*60*1000);
    }
}
