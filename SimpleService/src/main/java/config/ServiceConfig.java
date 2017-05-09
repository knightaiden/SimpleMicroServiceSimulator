package config;

import org.aiden.lab.microserve.calculator.CalculatorService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.annotation.Configuration;
import service.CalculatorServiceImpl;

/**
 * Created by zhangzhe on 2017/5/9.
 */
@Configuration
public class ServiceConfig {

    public static final int SERVER_PORT = 9000;

    public TServer calculatorTServer() throws TTransportException {
        TProcessor tprocessor =
                new CalculatorService.Processor<CalculatorService.Iface>
                        (new CalculatorServiceImpl());
        TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
        TServer.Args tArgs = new TServer.Args(serverTransport);
        tArgs.processor(tprocessor);
        tArgs.protocolFactory(new TBinaryProtocol.Factory());
        TServer server = new TSimpleServer(tArgs);
        return server;
    }

}
