package com.ygf.protocol.jessie.service;

import com.ygf.protocol.jessie.api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rpc服务提供者
 *
 * @author theo
 * @date 20181220
 */
public class ServiceImpl implements Service {
    private static Logger logger = LoggerFactory.getLogger(ServiceImpl.class);

    @Override
    public Integer test() {
        logger.info("in test()");
        return 100;
    }

    @Override
    public Integer test(Integer a, String b) {
        logger.info("in test(Integer, String)");
        return a + 100;
    }
}
