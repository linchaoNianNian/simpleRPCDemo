package rpc.demo.server;

import rpc.demo.annotation.Service;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RpcServer {
    public void start(int port,String clazz){
        ServerSocket server = null;
        try {
            //1、创建socket连接
            server = new ServerSocket(port);
            //2、获取所有服务类
            Map<String,Object> services = getService(clazz);
            //3、创建线程池
            Executor executor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>() {
            });
            while(true){
                //4、获取客户端连接
                Socket client = server.accept();
                //5、将服务端被调用的服务放到线程池中异步执行
                RpcServerHandler service = new RpcServerHandler(client,services);
                executor.execute(service);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (server!=null){
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*扫描包路径获取rpc服务类,并构建实例，与全限定名对应存在map里*/
    private Map<String, Object> getService(String clazz) throws ClassNotFoundException {
        if (clazz == null) {
            throw new ClassNotFoundException("扫描包名为空");
        }
        Map<String,Object> services = new HashMap<>();
        //全限定名数组
        String[] clazzes = clazz.split(",");

        try {
            List<Class<?>> classes = new ArrayList<>();
            for (String cl:clazzes) {
                List<Class<?>> classList = getClasses(cl);
                classes.addAll(classList);
            }
            //通过反射循环创建示例
            for (Class<?> cla:classes) {
                Object object = cla.newInstance();
                services.put(cla.getAnnotation(Service.class).value().getName(),object);
            }
        }catch (InstantiationException | IllegalAccessException | ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return services;
    }

    private  List<Class<?>> getClasses(String packageName) throws ClassNotFoundException {
        //定义返回的列表
        List<Class<?>> classes = new ArrayList<>();
        //找到指定的包目录
        File directory = null;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) {
            throw new ClassNotFoundException("无法获取到ClassLoader");
        }
        String path = packageName.replace(".","/");
        //TODO 处理目录含中文或者空格的问题
        URL resource = contextClassLoader.getResource(path);
        if (resource == null) {
            throw new ClassNotFoundException("无法获取该资源("+packageName+")");
        }
        directory = new File(resource.getFile());

        if(directory.exists()){
            //获取包目录留下所有文件
            String[] files = directory.list();
            File[] fileList = directory.listFiles();
            for (int i = 0; fileList!=null&&i<fileList.length; i++) {
                if ( null == files[i]) {break;}
                File file = fileList[i];
                //判断是否为class文件
                if (file.isFile()&&file.getName().endsWith(".class")){
                    Class<?> clazz = Class.forName(packageName+"."+files[i].substring(0,files[i].length()-6));
                    if (clazz.getAnnotation(Service.class)!=null) {//如果有@Service注解，添加到列表
                        classes.add(clazz);
                    }
                }else if(file.isDirectory()){    //如果是目录，则递归查找
                    List<Class<?>> classList = getClasses(packageName+"."+file.getName());
                    if (classList != null && classList.size()!=0) {
                        classes.addAll(classList);
                    }
                }
            }
        }else{
            throw new ClassNotFoundException("资源不存在"+directory);
        }
        return classes;
    }
}
