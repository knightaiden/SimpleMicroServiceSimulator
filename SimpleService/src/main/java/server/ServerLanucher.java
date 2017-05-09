package server;

import entity.ServiceDetails;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.thrift.server.TServer;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhangzhe on 2017/5/9.
 */
public class ServerLanucher {

    private int registerDeferTime = 2000;
    private int unregistertime = 5000;

    private CuratorFramework framework = null;
    private TServer tServer = null;
    private ServiceDiscovery<ServiceDetails> discovery = null;
    private boolean isRegistered = false;

    public void start() {
        new Thread (() ->{
            try {
                TimeUnit.MILLISECONDS
                        .sleep(registerDeferTime);

            } catch (InterruptedException e) {
                return;

            }
            try {
                framework.start();
                discovery.start();
                isRegistered = true;
            } catch (Exception ex) {
                return;
            }
        }).start();
        tServer.serve();
    }

    public void stop() throws InterruptedException {
        try {
            if (isRegistered) {
                discovery.close();
                framework.close();
            }
            isRegistered = false;
            TimeUnit.MILLISECONDS.sleep(registerDeferTime);
        } catch (Exception e) {
        }
        if(tServer.isServing()) {
            tServer.stop();
        }
    }

    public int getRegisterDeferTime() {
        return registerDeferTime;
    }

    public void setRegisterDeferTime(int registerDeferTime) {
        this.registerDeferTime = registerDeferTime;
    }

    public int getUnregistertime() {
        return unregistertime;
    }

    public void setUnregistertime(int unregistertime) {
        this.unregistertime = unregistertime;
    }

    public CuratorFramework getFramework() {
        return framework;
    }

    public void setFramework(CuratorFramework framework) {
        this.framework = framework;
    }

    public TServer gettServer() {
        return tServer;
    }

    public void settServer(TServer tServer) {
        this.tServer = tServer;
    }

    public ServiceDiscovery<ServiceDetails> getDiscovery() {
        return discovery;
    }

    public void setDiscovery(ServiceDiscovery<ServiceDetails> discovery) {
        this.discovery = discovery;
    }
}
