package com.ygf.protocol;

import com.ygf.tinyrpc.common.IdGenertor;
import com.ygf.tinyrpc.protocol.dubbo.code.DubboMessageToByteEncoder;
import com.ygf.tinyrpc.protocol.dubbo.message.DubboRequestMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JunitTest {
    private DubboMessageToByteEncoder encoder;
    private DubboRequestMessage msg;

    @Before
    public void setup(){
        System.out.println("222");
        encoder = new DubboMessageToByteEncoder();
        msg = new DubboRequestMessage();
        msg = new DubboRequestMessage();
        msg.setRequestId(IdGenertor.incrementAndGet());
        msg.setServiceName("com.ygf.protocol.DubboEncoder.test()");
        List<String> paramNames = new ArrayList<String>();
        paramNames.add("arg1");
        paramNames.add("arg2");
        paramNames.add("arg3");
        msg.setParamNames(paramNames);
        List<Object> params = new ArrayList<Object>();
        params.add((Integer)1);
        params.add((Integer)2);
        params.add((Integer)3);
        msg.setParams(params);
    }
    @Test
    public void test(){
        System.out.println("111");
    }
}
