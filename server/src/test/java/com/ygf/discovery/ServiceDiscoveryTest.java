package com.ygf.discovery;

import com.ygf.tinyrpc.discovery.ServiceDiscovery;
import org.junit.Before;

/**
 * 服务发现单元测试类
 *
 * @author theo
 * @date 20190228
 */
public class ServiceDiscoveryTest {
    private ServiceDiscovery discovery;

    @Before
    public void setup(){
        discovery = new ServiceDiscovery();
    }

}
