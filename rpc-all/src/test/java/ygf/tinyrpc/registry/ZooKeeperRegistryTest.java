package ygf.tinyrpc.registry;


import org.I0Itec.zkclient.ZkClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * 注册中心的测试类
 *
 * @author theo
 * @date 20190227
 */
public class ZooKeeperRegistryTest {
    /**
     * url
     */
    private String zkUrl;
    /**
     * registry
     */
    private ZooKeeperRegistry registry;
    /**
     * zkClient
     */
    private ZkClient zkClient;

    @Before
    public void setup() throws Exception {
        zkUrl = "192.168.1.101:2181";
        registry = new ZooKeeperRegistry(zkUrl, true);
        Field field = ZooKeeperRegistry.class.getDeclaredField("zkClient");
        field.setAccessible(true);
        // mock
        zkClient = mock(ZkClient.class);
        field.set(registry, zkClient);
    }

    /**
     * 创建永久节点
     */
    @Test
    public void createPersistent() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                Arrays.asList(args).forEach(System.out::println);
                return "called with args:" + args;
            }
        }).when(zkClient).createPersistent(anyString(), anyBoolean());

        String path = "/rpc";
        registry.createPersistent(path, true);
    }

    /**
     * 创建临时节点
     */
    @Test
    public void createEphemeral(){
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                Arrays.asList(args).forEach(System.out::println);
                return "called with args:" + args;
            }
        }).when(zkClient).createEphemeral(anyString());

        String path = "/rpc";
        registry.createEphemeral(path);
    }
}

