package rpc.demo.test;

import rpc.demo.server.RpcServer;

public class ServerTest {
    public static void main(String[] args) {
        RpcServer server= new RpcServer();
        server.start(9999,"rpc.demo.taskdemo");
    }
}
