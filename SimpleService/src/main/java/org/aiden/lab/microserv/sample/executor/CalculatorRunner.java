package org.aiden.lab.microserv.sample.executor;

import org.aiden.lab.microserv.sample.server.ServerLanucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * Created by zhangzhe on 2017/5/17.
 */
@Service
public class CalculatorRunner implements CommandLineRunner {

    @Autowired
    private ServerLanucher serverLanucher;

    @Override
    public void run(String... strings) throws Exception {
        serverLanucher.start();
    }

}
