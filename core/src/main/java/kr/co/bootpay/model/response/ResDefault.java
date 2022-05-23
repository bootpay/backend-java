package kr.co.bootpay.model.response;

import com.google.gson.Gson;

/**
 * Created by ehowlsla on 2018. 5. 29..
 */
public class ResDefault<T> {
    public int status;
    public int code;
    public String message;
    public T data;

    public String toJson() {
        return new Gson().toJson(this);
    }
}
