# tinyrpc
this is a base rpc framework which can be used to develop service providers and consumers. 

# Features
* base on netty4
* service discovery with zookeeper

# How to build
this lib is base on maven, so just:
```
cd tinyrpc;
mvn clean install
```

then add dependency to pom.xml:
```
    <dependency>
        <groupId>com.ygf</groupId>
        <artifactId>tinyrpc</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>    
```

# How to use
## define service api
```
    public interface DemoService {
        Integer sayHello();
    }
```

## define service imlement
```
    public class DemoServiceImpl implements DemoService {
        private static Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

        @Override
        public Integer sayHello() {
            logger.info("in test()");
            return 100;
        }
    }
```

## start zookeeper service
for example, run on 192.168.0.104:2181


## start service provider
```
    public  static void main(String[] args) throws Exception{
        DemoService testService = new DemoServiceImpl();
        ServiceConfig service = new ServiceConfig();

        ApplicationConfig application = new ApplicationConfig();
        application.setApplicationName("test");
        service.setApplication(application);

        RegistryConfig registry = new RegistryConfig();
        registry.setType("zookeeper");
        registry.setAddress("192.168.0.104:2181");

        service.setRegistry(registry);

        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("jessie");
        protocol.setHost("192.168.0.104");
        protocol.setPort(20880);
        service.setProtocol(protocol);

        service.setInterface(DemoService.class);
        service.setRef(testService);
        // export service to zookeeper
        RpcContextUtils.export(service);
        // waiting
        Thread.sleep(60*60*1000);
    }
```
    
## start service consumer
```
    public static void main(String[] args) throws Exception{
        ReferenceConfig reference = new ReferenceConfig();

        ApplicationConfig application = new ApplicationConfig();
        application.setApplicationName("reference-test");
        reference.setApplication(application);

        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("192.168.43.98:2181");
        registry.setType("zookeeper");
        reference.setRegistry(registry);

        reference.setInterface(DemoService.class);
        DemoService ref = (DemoService) RpcContextUtils.get(reference);
        System.out.println(ref.sayHello());
        // waiting
        Thread.sleep(60*60*1000);
    }
```
