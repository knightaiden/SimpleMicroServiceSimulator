import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import server.ServerLanucher;

/**
 * Created by zhangzhe on 2017/5/9.
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private ServerLanucher serverLanucher;

    @Override
    public void run(String... strings) throws Exception {
        serverLanucher.start();
    }
}
