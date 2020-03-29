package ygf.tinyrpc.example.server;

import ygf.tinyrpc.example.api.DemoService;
import ygf.tinyrpc.example.service.DemoServiceImpl;
import ygf.tinyrpc.config.ApplicationConfig;
import ygf.tinyrpc.config.ProtocolConfig;
import ygf.tinyrpc.config.RegistryConfig;
import ygf.tinyrpc.config.ServiceConfig;
import ygf.tinyrpc.utils.RpcContextUtils;

public class Server {
    public  static void main(String[] args) throws Exception{
        DemoService testService = new DemoServiceImpl();
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

        service.setInterface(DemoService.class);
        service.setRef(testService);
        // 暴露服务
        RpcContextUtils.export(service);
        // 等待远程调用
        Thread.sleep(60*60*1000);
    }
}
