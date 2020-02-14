package ygf.tinyrpc.example.service;

import ygf.tinyrpc.example.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rpc服务提供者
 *
 * @author theo
 * @date 20181220
 */
public class DemoServiceImpl implements DemoService {
    private static Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public Integer sayHello() {
        logger.info("in test()");
        return 100;
    }
}


