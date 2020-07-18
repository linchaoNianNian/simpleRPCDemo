package rpc.demo.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpc.demo.api.RpcRequest;
import rpc.demo.server.RpcServerHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {
    private static Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    private String host;
    private int port;

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){//clazz必须是接口类型
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},RpcClientProxy.this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //这里可以功能增强,比如负载均衡，过滤器
        logger.info("调用远程方法前，xxxxxx");
        RpcRequest request = new RpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParamTypes(method.getParameterTypes());
        request.setParams(args);
        Object result = new RpcClient().execute(request, host, port);
        //功能增强，比如记录流水信息
        logger.info("调用远程方法后，xxxxxx");
        return result;
    }
}

