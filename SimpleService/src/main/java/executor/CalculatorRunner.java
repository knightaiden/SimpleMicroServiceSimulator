package executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import server.ServerLanucher;

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
