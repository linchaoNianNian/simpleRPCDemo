package rpc.demo.api;

import java.io.Serializable;

public class RpcRequest implements Serializable {
    //接口全限定名
    private String className;
    //方法名
    private String methodName;
    //方法参数类型
    private Class<?>[] paramTypes;
    //方法参数
    private Object[] params;

    /*getter and setter*/

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
