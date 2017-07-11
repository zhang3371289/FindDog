package com.find.dog.data;

import java.io.Serializable;

/**
 * Created by zhangzhongwei on 2017/5/9.
 */

public class UserInfoUpdate implements Serializable {

    /**
     * avatar : {"id":"0","width":190,"height":190,"url":"http://ss1.yokacdn.com/cosmetics/fujun/img/touxiang_default.png","mimetype":"image/png"}
     * username : 林紫一
     * resideprovince : 北京
     * residecity :
     * email :
     * user_level : 普通会员
     * nickname : 林紫一
     * "user_type": 4,
     * sex : {"id":"2","value":"女"}
     * skin_type : {"id":"","value":""}
     * beauty_month_consume : {"id":"","value":""}
     * birth_str :
     * hair_type : {"id":"","value":""}
     * prink_custom : {"id":"","value":""}
     * brand_loc : [{"id":"1","value":"欧美系品牌粉丝"},{"id":"2","value":"偏好日韩系品牌"},{"id":"10","value":"其它"}]
     * product : [{"id":"2","value":"彩妆"},{"id":"5","value":"美体瘦身"}]
     */

    private String username;
    private String resideprovince;
    private String residecity;
    private String email;
    private String user_level;
    private String nickname;
    private int user_type;
    private String birth_str;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getResideprovince() {
        return resideprovince;
    }

    public void setResideprovince(String resideprovince) {
        this.resideprovince = resideprovince;
    }

    public String getResidecity() {
        return residecity;
    }

    public void setResidecity(String residecity) {
        this.residecity = residecity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }


    @Override
    public String toString() {
        return "UserInfoUpdate{" +
                ", username='" + username + '\'' +
                ", resideprovince='" + resideprovince + '\'' +
                ", residecity='" + residecity + '\'' +
                ", email='" + email + '\'' +
                ", user_level='" + user_level + '\'' +
                ", nickname='" + nickname + '\'' +
                ", user_type='" + user_type + '\'' +
                '}';
    }


}
