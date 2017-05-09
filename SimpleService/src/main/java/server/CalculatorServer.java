package server;

import org.aiden.lab.microserve.calculator.CalculatorService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import service.CalculatorServiceImpl;

/**
 * Created by zhangzhe on 2017/5/9.
 */
public class CalculatorServer {

    public static final int SERVER_PORT = 9000;

    public void startServer() {
        try {
            System.out.println("CalculatorService TSimpleServer start ....");

            TProcessor tprocessor =
                    new CalculatorService.Processor<CalculatorService.Iface>
                            (new CalculatorServiceImpl());
            TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
            TServer.Args tArgs = new TServer.Args(serverTransport);
            tArgs.processor(tprocessor);
            tArgs.protocolFactory(new TBinaryProtocol.Factory());
            TServer server = new TSimpleServer(tArgs);
            server.serve();
        } catch (Exception e) {
            System.out.println("Server start error!!!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CalculatorServer server = new CalculatorServer();
        server.startServer();
    }

}
