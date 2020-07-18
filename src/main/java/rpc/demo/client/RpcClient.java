package rpc.demo.client;

import rpc.demo.api.RpcRequest;
import rpc.demo.api.RpcResponse;

import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RpcClient {
    public Object execute(RpcRequest request, String host, int port) throws Throwable {
        Socket server = new Socket(host, port);
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try{
            //将请求写到连接服务端socket的输出流
            oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(request);
            oos.flush();

            //读取输入流的内容
            ois = new ObjectInputStream(server.getInputStream());
            Object res = ois.readObject();
            RpcResponse response = null;
            if (!(res instanceof RpcResponse)){
                throw new InvalidClassException("相应类型不正确，应当为"+RpcResponse.class+"类型");
            }else{
                response = (RpcResponse) res;
            }
            if (response.getError()!=null){
                throw response.getError();
            }
            return response.getResult();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (ois!=null)ois.close();
            if (oos != null) oos.close();
            if (server != null) server.close();
        }
    }
}
