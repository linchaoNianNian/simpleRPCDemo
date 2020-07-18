package rpc.demo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.demo.api.RpcRequest;
import rpc.demo.api.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class RpcServerHandler implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);
    private Socket clientSocket;
    private Map<String,Object> serviceMap;
    public RpcServerHandler(Socket client, Map<String, Object> services) {
        this.clientSocket = client;
        this.serviceMap = services;
    }

    @Override
    public void run() {
        logger.info("-----------------RpcServerHandler-----------");
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        RpcResponse response = new RpcResponse();
        try{
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());

            //反序列化
            Object object = ois.readObject();
            RpcRequest request = null;
            if (!(object instanceof RpcRequest)){
                response.setError(new Exception("请求类型错误"));
                oos.writeObject(response);
                oos.flush();
                return;
            }else {
                request = (RpcRequest) object;
            }
            //查找并执行服务
            Object service = serviceMap.get(request.getClassName());
            logger.info("ClassName:"+request.getClassName());
            Class<?> clazz = service.getClass();
            Method method = clazz.getMethod(request.getMethodName(),request.getParamTypes());
            logger.info("MethodName:"+request.getMethodName());
            Object result = method.invoke(service,request.getParams());

            response.setResult(result);
            oos.writeObject(response);
            oos.flush();

        } catch (Exception  e) {
            if (oos != null) {
                response.setError(e);
                try {
                    oos.writeObject(response);
                    oos.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            try {
                if (ois!=null) ois.close();
                if (oos != null) oos.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

