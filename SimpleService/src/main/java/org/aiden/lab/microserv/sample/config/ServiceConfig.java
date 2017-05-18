package org.aiden.lab.microserv.sample.config;

import org.aiden.lab.microserv.sample.entity.ServiceDetails;
import org.aiden.lab.microserv.sample.service.CalculatorServiceImpl;
import org.aiden.lab.microserve.calculator.CalculatorService;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.aiden.lab.microserv.sample.server.ServerLanucher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhe on 2017/5/9.
 */
@Configuration
public class ServiceConfig {

    public static final int SERVER_PORT = 9000;

    @Autowired
    private Environment env;


    public ServiceDetails payload(){
        ServiceDetails serviceDetails = new ServiceDetails();
        serviceDetails.setId("1");
        serviceDetails.setInterfaceName("cal1");
        serviceDetails.setListenAddress("localhost");
        serviceDetails.setListenPort(SERVER_PORT);
        return serviceDetails;
    }

    /**
     * 4 option for the TServer(exclude the TSampleServer)
     * 1. TNonblockingServer -  Single thread, NIO for all the socket in selector
     * 2. THsHaServer - TNonblockingServer with thread pool
     * 3. TThreadPoolServer - Blocking socket, the main thread used for listen the socket
     * 4. TThreadedSelectorServer - Advanced usage, recommended !
     */
    @Bean
    public TServer calculatorTServer() throws TTransportException {
        TProcessor tprocessor =
                new CalculatorService.Processor<CalculatorService.Iface>
                        (new CalculatorServiceImpl());
        TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(SERVER_PORT);
        TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(serverTransport);
        tArgs.processor(tprocessor);
        tArgs.protocolFactory(new TBinaryProtocol.Factory());
        //tArgs.transportFactory(new TFramedTransport.Factory());
        TServer server = new TThreadPoolServer(tArgs);
        return server;
    }

    public ACLProvider aclProvider() {
        return new ACLProvider() {
            @Override
            public List<ACL> getDefaultAcl() {
                return ZooDefs.Ids.CREATOR_ALL_ACL;
            }

            @Override
            public List<ACL> getAclForPath(String path) {
                return ZooDefs.Ids.CREATOR_ALL_ACL;
            }
        };
    }


    public List<AuthInfo> authInfo() {
        String scheme = env.getProperty("rpc.server.zookeeper.scheme");
        String auth = env.getProperty("rpc.server.zookeeper.auth");
        List<AuthInfo> info = new ArrayList();
        info.add(new AuthInfo(scheme, auth.getBytes()));
        return info;
    }


    public RetryPolicy retryPolicy() {
        int baseSleepTimeMs = Integer.parseInt(env.getProperty(
                "rpc.server.zookeeper.base.sleep.time.ms", "1000"));
        int maxSleepTimeMs = Integer.parseInt(env.getProperty(
                "rpc.server.zookeeper.max.sleep.time.ms", "5000"));
        int maxRetries = Integer.parseInt(env.getProperty(
                "rpc.server.zookeeper.max.retries", "50"));
        return new BoundedExponentialBackoffRetry(baseSleepTimeMs,
                maxSleepTimeMs, maxRetries);
    }

    @Bean
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory
                .builder()
                .connectString(
                        env.getProperty("rpc.server.zookeeper.connect.string"))
                .sessionTimeoutMs(50000)
                .connectionTimeoutMs(50000)
                .retryPolicy(this.retryPolicy())
                .aclProvider(this.aclProvider())
                .authorization(this.authInfo())
                .build();
    }

    @Bean
    public ServiceInstance<ServiceDetails> serviceInstance() throws Exception {
        ServiceInstanceBuilder<ServiceDetails> instance = ServiceInstance.builder();
        instance.name(env.getProperty("rpc.server.service.name"))
                .uriSpec(new UriSpec(env.getProperty("rpc.server.uri.spec")))
                .payload(this.payload())
                .port(env.getProperty("rpc.server.port", Integer.class))
                .id(env.getProperty("rpc.server.port")+":"+env.getProperty("rpc.server.ip"))
                .address(env.getProperty("rpc.server.ip"));
        return instance.build();
    }

    @Bean
    public ServiceDiscovery<ServiceDetails> serviceDiscovery(CuratorFramework curatorFramework) throws Exception {
        ServiceDiscoveryBuilder<ServiceDetails> builder = ServiceDiscoveryBuilder
                .builder(ServiceDetails.class);
        return builder
                .client(curatorFramework)
                .basePath(env.getProperty("rpc.server.service.path"))
                .serializer(new JsonInstanceSerializer(ServiceDetails.class))
                .thisInstance(this.serviceInstance()).build();
    }

    @Bean(destroyMethod="stop")
    public ServerLanucher serverLanucher(ServiceDiscovery serviceDiscovery,
                                         CuratorFramework curatorFramework,
                                         TServer calculatorTServer){
        ServerLanucher lanucher = new ServerLanucher();
        lanucher.setDiscovery(serviceDiscovery);
        lanucher.setFramework(curatorFramework);
        lanucher.settServer(calculatorTServer);
        return lanucher;
    }

}
