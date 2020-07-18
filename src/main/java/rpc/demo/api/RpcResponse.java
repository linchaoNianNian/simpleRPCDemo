package rpc.demo.api;

import java.io.Serializable;

public class RpcResponse implements Serializable {
    //异常
    private Throwable error;
    //调用结果
    private Object result;

    /*getter and setter*/

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}

