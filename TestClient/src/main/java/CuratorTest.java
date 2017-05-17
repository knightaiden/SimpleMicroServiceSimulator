import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.omg.CORBA.ServiceDetail;

import java.util.Collection;

/**
 * Created by zhangzhe on 2017/5/17.
 */
public class CuratorTest {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        client.blockUntilConnected();
        ServiceDiscovery<ServiceDetail> serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceDetail.class)
                .client(client)
                .basePath("/test/calculator")
                .build();
        serviceDiscovery.start();
        Collection<ServiceInstance<ServiceDetail>> services = serviceDiscovery.queryForInstances("calculator");
        System.out.println(services);
    }

}
