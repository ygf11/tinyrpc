package ygf.tinyrpc.export;

import ygf.tinyrpc.jessie.api.Service;
import ygf.tinyrpc.config.ApplicationConfig;
import ygf.tinyrpc.config.ProtocolConfig;
import ygf.tinyrpc.config.RegistryConfig;
import ygf.tinyrpc.config.ServiceConfig;
import ygf.tinyrpc.registry.ZooKeeperRegistry;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * 服务暴露测试代码
 *
 * @author theo
 * @date 20190227
 */
public class ExportTest {
    /**
     * 服务暴露类
     */
    private Exporter exporter;
    /**
     * 暴露服务配置
     */
    private ServiceConfig service;
    /**
     * 注册中心
     */
    private ZooKeeperRegistry registry;

    @Before
    public void setup() {
        exporter = new Exporter();

        registry = mock(ZooKeeperRegistry.class);

        service = new ServiceConfig();

        ApplicationConfig application = new ApplicationConfig();
        application.setApplicationName("export-test");
        service.setApplication(application);

        RegistryConfig registry = new RegistryConfig();
        registry.setType("zookeeper");
        registry.setAddress("192.168.1.101:2080");
        service.setRegistry(registry);

        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("rpc");
        protocol.setHost("192.168.1.101");
        protocol.setPort(20880);
        service.setProtocol(protocol);

        service.setInterface(Service.class);
    }


    /**
     * getMethods()方法测试
     */
    @Test
    public void getMethodsTest() throws Exception {
        Object[] args = new Object[1];
        args[0] = "ygf.tinyrpc.jessie.api.Service";
        Class[] classes = new Class[1];
        classes[0] = String.class;
        String res = (String) MethodUtils.invokeMethod(exporter, true, "getMethods", args, classes);
        Assert.assertEquals(res, "test,test");
    }

    /**
     * 测试执行服务暴露(Exporter.doExport())
     */
    @Test
    public void doExport() throws Exception{
        Object[] args = new Object[2];
        args[0] = service;
        args[1] = registry;

        Class[] classes = new Class[2];
        classes[0] = ServiceConfig.class;
        classes[1] = ZooKeeperRegistry.class;

        doAnswer(invocationOnMock -> {
            Arrays.asList(invocationOnMock.getArguments()).forEach(System.out::println);
            return null;
        }).when(registry).createEphemeral(anyString());

        MethodUtils.invokeMethod(exporter, true, "doExport", args, classes);
    }
}
