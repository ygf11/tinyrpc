package ygf.tinyrpc.discovery;

import ygf.tinyrpc.jessie.api.Service;
import ygf.tinyrpc.context.RpcProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * zk服务节点数据解析单元测试代码
 *
 * @author theo
 * @date 20190228
 */
public class ProviderParserTest {
    private ProviderParser parser;
    private String ip = "192.168.1.101";
    private String iName = Service.class.getCanonicalName();
    private Integer port = 20880;
    private String appName = "parser-test";
    private String methods = "test,test";

    @Before
    public void setup() {
        parser = new ProviderParser();
    }

    @Test
    public void parseTest() {
        String url = "rpc://" + ip
                + "/" + iName
                + "?" + "port=" + port
                + "?" + "appName=" + appName
                + "?" + "interface=" + iName
                + "?" + "methods=" + methods;
        RpcProvider provider = parser.parse(url);
        Assert.assertEquals(ip, provider.getIp());
        Assert.assertEquals(appName, provider.getAppName());
        Assert.assertEquals(iName, provider.getName());
        Assert.assertEquals(Service.class, provider.getService());
        for (String method : provider.getMethods()) {
            Assert.assertEquals("test", method);
        }
    }
}
