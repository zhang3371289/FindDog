package com.find.dog.data;

import java.io.Serializable;

/**
 * Created by zhangzhongwei on 2017/7/19.
 */

public class stringInfo implements Serializable {

    /**
     * info : success!
     */

    private String info;
    private String erro;

    public String getInfo() {
        return info;
    }
    public String getErro() {
        return erro;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public void setErro(String erro) {
        this.erro = erro;
    }

    @Override
    public String toString() {
        return "stringInfo{" +
                "info='" + info + '\'' +
                "erro='" + erro + '\'' +
                '}';
    }
}
