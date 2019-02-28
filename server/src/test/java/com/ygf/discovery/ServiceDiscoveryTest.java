package com.ygf.discovery;

import com.ygf.protocol.jessie.api.Service;
import com.ygf.tinyrpc.config.ApplicationConfig;
import com.ygf.tinyrpc.config.ReferenceConfig;
import com.ygf.tinyrpc.config.RegistryConfig;
import com.ygf.tinyrpc.context.RpcProvider;
import com.ygf.tinyrpc.discovery.ServiceDiscovery;
import com.ygf.tinyrpc.registry.ZooKeeperRegistry;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * 服务发现单元测试类
 *
 * @author theo
 * @date 20190228
 */
public class ServiceDiscoveryTest {
    private String ip = "192.168.1.101";
    private String name = Service.class.getCanonicalName();
    private Integer port = 20880;
    private String appName = "provider";
    private String interfaceName = name;
    private String methods = "test,test";

    private String zkUrl = "192.168.1.101:2080";
    private ServiceDiscovery discovery;
    private ZooKeeperRegistry registry;
    private ReferenceConfig reference;

    @Before
    public void setup() throws IllegalAccessException {
        discovery = new ServiceDiscovery();
        registry = mock(ZooKeeperRegistry.class);
        Field field = FieldUtils.getField(ServiceDiscovery.class, "registryMap", true);
        Map<String, ZooKeeperRegistry> map = (Map<String, ZooKeeperRegistry>) field.get(discovery);
        map.put(zkUrl, registry);

        initReferenceConfig();
    }

    /**
     * 初始化reference
     */
    private void initReferenceConfig() {
        reference = new ReferenceConfig();
        ApplicationConfig application = new ApplicationConfig();
        application.setApplicationName("discovery-test");
        reference.setApplication(application);

        RegistryConfig registry = new RegistryConfig();
        registry.setType("zookeeper");
        registry.setAddress("192.168.1.101:2080");
        reference.setRegistry(registry);

        reference.setInterface(Service.class.getCanonicalName());
    }

    /**
     * 从zk获取provider的测试(节点存在)
     */
    @Test
    public void getFromRegistryTest() throws Exception {
        String url = "rpc://" + ip
                + "/" + name
                + "?" + "port=" + port
                + "?" + "appName=" + appName
                + "?" + "interface=" + interfaceName
                + "?" + "methods=" + methods;
        List<String> list = new ArrayList<>();
        list.add(url);
        when(registry.getChildren(anyString())).thenReturn(list);
        when(registry.exists(anyString())).thenReturn(true);
        Object[] args = new Object[1];
        args[0] = reference;
        Class[] classes = new Class[1];
        classes[0] = ReferenceConfig.class;

        List<RpcProvider> providers = (List<RpcProvider>) MethodUtils.
                invokeMethod(discovery, true, "getProvidersFromRegistry", args, classes);

        Assert.assertEquals(1, providers.size());
        RpcProvider provider = providers.get(0);
        Assert.assertEquals(name, provider.getName());
        Assert.assertEquals(ip, provider.getIp());
        Assert.assertEquals(port, provider.getPort());
        Assert.assertEquals(appName, provider.getAppName());
        Assert.assertEquals(Service.class, provider.getService());

        provider.getMethods().stream().forEach(x ->Assert.assertEquals("test", x));

    }
}
