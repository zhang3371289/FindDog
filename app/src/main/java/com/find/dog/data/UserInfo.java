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
     * pay :
     */

    public static int WECHATPAY = 1, ALIPAY = 2;

    private String name;
    private String phone;
    private String adress;
    private String pay_number;
    private int pay_type = 0;

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

    public String getPayNumber() {
        return pay_number;
    }

    public void setPayNumber(String pay_number) {
        this.pay_number = pay_number;
    }

    public int getPayType() {
        return pay_type;
    }

    public void setPayType(int pay_type) {
        this.pay_type = pay_type;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", adress='" + adress + '\'' +
                ", pay_number='" + pay_number + '\'' +
                ", pay_type='" + pay_type + '\'' +
                '}';
    }


}
