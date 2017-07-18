package com.find.dog.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zhangzhongwei on 2017/5/9.
 */

public class UserInfoUpdate implements Serializable {


    /**
     * id : 2
     * masterPhone : 18801308610
     * hunterPhone : 1561235478
     * patName : 窝头
     * 2dCode : 123457
     * state : confirming
     * reward : 4000
     * describ : Its my baby dog!Please go home!
     * loseAddress : 山东省青岛市城阳区6号大街
     * loseDate : 1500380345
     * backDate : 0
     * alipay :
     * wechet :
     * photo1URL : www.sina.com.cn
     * photo2URL : www.sina.com.cn
     * photo3URL : www.sina.com.cn
     */

    private String id;
    private String masterPhone;
    private String hunterPhone;
    private String patName;
    @SerializedName("2dCode")
    private String _$2dCode;
    private String state;
    private String reward;
    private String describ;
    private String loseAddress;
    private String loseDate;
    private String backDate;
    private String alipay;
    private String wechet;
    private String photo1URL;
    private String photo2URL;
    private String photo3URL;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMasterPhone() {
        return masterPhone;
    }

    public void setMasterPhone(String masterPhone) {
        this.masterPhone = masterPhone;
    }

    public String getHunterPhone() {
        return hunterPhone;
    }

    public void setHunterPhone(String hunterPhone) {
        this.hunterPhone = hunterPhone;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String get_$2dCode() {
        return _$2dCode;
    }

    public void set_$2dCode(String _$2dCode) {
        this._$2dCode = _$2dCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public String getLoseAddress() {
        return loseAddress;
    }

    public void setLoseAddress(String loseAddress) {
        this.loseAddress = loseAddress;
    }

    public String getLoseDate() {
        return loseDate;
    }

    public void setLoseDate(String loseDate) {
        this.loseDate = loseDate;
    }

    public String getBackDate() {
        return backDate;
    }

    public void setBackDate(String backDate) {
        this.backDate = backDate;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getWechet() {
        return wechet;
    }

    public void setWechet(String wechet) {
        this.wechet = wechet;
    }

    public String getPhoto1URL() {
        return photo1URL;
    }

    public void setPhoto1URL(String photo1URL) {
        this.photo1URL = photo1URL;
    }

    public String getPhoto2URL() {
        return photo2URL;
    }

    public void setPhoto2URL(String photo2URL) {
        this.photo2URL = photo2URL;
    }

    public String getPhoto3URL() {
        return photo3URL;
    }

    public void setPhoto3URL(String photo3URL) {
        this.photo3URL = photo3URL;
    }

    @Override
    public String toString() {
        return "UserInfoUpdate{" +
                "id='" + id + '\'' +
                ", masterPhone='" + masterPhone + '\'' +
                ", hunterPhone='" + hunterPhone + '\'' +
                ", patName='" + patName + '\'' +
                ", _$2dCode='" + _$2dCode + '\'' +
                ", state='" + state + '\'' +
                ", reward='" + reward + '\'' +
                ", describ='" + describ + '\'' +
                ", loseAddress='" + loseAddress + '\'' +
                ", loseDate='" + loseDate + '\'' +
                ", backDate='" + backDate + '\'' +
                ", alipay='" + alipay + '\'' +
                ", wechet='" + wechet + '\'' +
                ", photo1URL='" + photo1URL + '\'' +
                ", photo2URL='" + photo2URL + '\'' +
                ", photo3URL='" + photo3URL + '\'' +
                '}';
    }
}
