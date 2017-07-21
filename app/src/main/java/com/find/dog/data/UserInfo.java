package com.find.dog.data;

import java.io.Serializable;

/**
 * Created by zhangzhongwei on 2017/7/21.
 */

public class UserInfo implements Serializable {


    /**
     * name :
     * phone : 13311313
     * adress :
     */

    private String name;
    private String phone;
    private String adress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    @Override
    public String toString() {
        return "stringInfo{" +
                "name='" + name + '\'' +
                ", phone=" + phone +
                ", adress='" + adress + '\'' +
                '}';
    }
}
