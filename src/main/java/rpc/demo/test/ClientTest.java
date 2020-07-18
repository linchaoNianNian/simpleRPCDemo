package rpc.demo.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.demo.client.RpcClientProxy;
import rpc.demo.api.Student;
import rpc.demo.server.RpcServerHandler;
import rpc.demo.taskdemo.StudentService;

public class ClientTest {
    private static Logger logger = LoggerFactory.getLogger(ClientTest.class);
    public static void main(String[] args) {
        logger.error("处理完毕");
        RpcClientProxy clientProxy = new RpcClientProxy("127.0.0.1", 9999);
        StudentService proxy = clientProxy.getProxy(StudentService.class);
//        System.out.println(proxy.getInfo());
        logger.info("处理完毕");
        for (int i=0;i<=10;i++){
            proxy.printInfo(new Student(i,"lisi"+i,18+i));
        }
    }
}
