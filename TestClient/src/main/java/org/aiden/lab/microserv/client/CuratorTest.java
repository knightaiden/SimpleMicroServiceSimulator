package org.aiden.lab.microserv.client;

import org.aiden.lab.microserve.calculator.CalculatorService;
import org.aiden.lab.microserve.calculator.Result;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.aiden.lab.microserv.sample.entity.ServiceDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhangzhe on 2017/5/17.
 */
public class CuratorTest {

    public static void main(String[] args) throws Exception {
        String scheme = "digest";
        String auth = "authkey";
        List<AuthInfo> info = new ArrayList();
        info.add(new AuthInfo(scheme, auth.getBytes()));

        int baseSleepTimeMs = Integer.parseInt("1000");
        int maxSleepTimeMs = Integer.parseInt("5000");
        int maxRetries = Integer.parseInt("50");
        BoundedExponentialBackoffRetry retry =  new BoundedExponentialBackoffRetry(baseSleepTimeMs,
                maxSleepTimeMs, maxRetries);

        ACLProvider provider = new ACLProvider() {
            @Override
            public List<ACL> getDefaultAcl() {
                return ZooDefs.Ids.CREATOR_ALL_ACL;
            }

            @Override
            public List<ACL> getAclForPath(String path) {
                return ZooDefs.Ids.CREATOR_ALL_ACL;
            }
        };

        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString("localhost:2181")
                .sessionTimeoutMs(50000)
                .connectionTimeoutMs(50000)
                .retryPolicy(retry)
                .aclProvider(provider)
                .authorization(info)
                .build();
        client.start();
        ServiceDiscovery<ServiceDetails> serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceDetails.class)
                .client(client)
                .basePath("/test/calculator")
                .build();
        serviceDiscovery.start();
        Collection<ServiceInstance<ServiceDetails>> services = serviceDiscovery.queryForInstances("calculator");
        ServiceInstance<ServiceDetails> instance = services.iterator().next();
        TTransport socket = new TSocket(instance.getAddress(), instance.getPort().intValue());
        TProtocol protocol = new TBinaryProtocol(socket);
        CalculatorService.Client calclient = new CalculatorService.Client(
                protocol);
        socket.open();
        Result result = calclient.add(1, 2);
        System.out.println("Thrify client result =: " + result);
    }

}
