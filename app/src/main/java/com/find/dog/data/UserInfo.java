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

    private String name;
    private String phone;
    private String adress;
    private String pay_money;
    private PayType pay_type;

    public static enum PayType {
        alipay, wechatpay
    }

    public void setPayType(PayType pay_type) {
        this.pay_type = pay_type;
    }

    public PayType getPayType() {
        return pay_type;
    }

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

    public String getPayMoney() {
        return pay_money;
    }

    public void setPayMoney(String pay_money) {
        this.pay_money = pay_money;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", adress='" + adress + '\'' +
                ", pay_money='" + pay_money + '\'' +
                ", pay_type=" + pay_type +
                '}';
    }
}
