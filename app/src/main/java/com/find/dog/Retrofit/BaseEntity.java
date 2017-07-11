package com.find.dog.Retrofit;

import java.io.Serializable;

public class BaseEntity<E> implements Serializable {
    private int code;
    private String message;
    private E result;//回头会跟服务器协商该字段统一改为'result'

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public E getData() {
        return result;
    }

    public void setData(E data) {
        this.result = data;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}