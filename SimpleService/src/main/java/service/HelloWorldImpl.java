package service;

import org.aiden.lab.microserv.demo.HelloWorldService;
import org.apache.thrift.TException;

/**
 * Created by zhangzhe on 2017/5/5.
 */
public class HelloWorldImpl implements HelloWorldService.Iface {

    public String sayHello(String username) throws TException {
        return "Hi," + username + "! welcome!";
    }
}
