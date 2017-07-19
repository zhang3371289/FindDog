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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "stringInfo{" +
                "info='" + info + '\'' +
                '}';
    }
}
