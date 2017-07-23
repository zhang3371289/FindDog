package com.find.dog.data;

/**
 * Created by ZhangV on 2017/7/23.
 */

public class GetUserInfo {

    /**
     * id : 6
     * userPhone :
     * homeAddress :
     * alipay :
     * wechatpay :
     * alive : 2
     */

    private String id;
    private String userPhone;
    private String homeAddress;
    private String alipay;
    private String wechatpay;
    private String alive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getWechatpay() {
        return wechatpay;
    }

    public void setWechatpay(String wechatpay) {
        this.wechatpay = wechatpay;
    }

    public String getAlive() {
        return alive;
    }

    public void setAlive(String alive) {
        this.alive = alive;
    }

    @Override
    public String toString() {
        return "GetUserInfo{" +
                "id='" + id + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", homeAddress='" + homeAddress + '\'' +
                ", alipay='" + alipay + '\'' +
                ", wechatpay='" + wechatpay + '\'' +
                ", alive='" + alive + '\'' +
                '}';
    }
}
