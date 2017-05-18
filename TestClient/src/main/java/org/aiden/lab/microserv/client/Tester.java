package org.aiden.lab.microserv.client;

import org.aiden.lab.microserve.calculator.CalculatorService;
import org.aiden.lab.microserve.calculator.Result;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by zhangzhe on 2017/5/5.
 */
public class Tester {

    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 9000;
    public static final int TIMEOUT = 30000;

    /**
     *
     * @param userName
     */
    public void startClient(String userName) {
        TTransport transport = null;
        try {
            transport = new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
            TProtocol protocol = new TBinaryProtocol(transport);
            CalculatorService.Client client = new CalculatorService.Client(
                    protocol);
            transport.open();
            Result result = client.add(1, 2);
            System.out.println("Thrify client result =: " + result);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Tester client = new Tester();
        client.startClient("Michael");
    }

}
