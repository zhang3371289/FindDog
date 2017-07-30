package com.find.dog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.find.dog.data.UserInfo;
import com.find.dog.data.UserPetInfo;
import com.find.dog.main.MyApplication;

import java.util.ArrayList;

/**
 * Created by zhangzhongwei on 2017/6/29.
 */

public class MyManger {
    public static final String BASE = "http://zhaogou.applinzi.com/";//正式环境

    private static final String user_islogin = "user_islogin";
    private static final String user_name = "user_name";
    private static final String user_adress = "user_adress";
    private static final String user_phone = "user_phone";
    private static final String user_pay_number = "user_pay_number";
    private static final String user_pay_type = "user_pay_type";
    private static final String pic_Status_size = "pic_Status_size";
    private static final String pic_Status_ = "pic_Status_";
    private static final String issue_money = "issue_money";
    private static final String pet_describ = "pet_describ";
    private static final String pet_QRCode = "pet_QRCode";
    private static final String pet_lost_time = "pet_lost_time";
    private static final String pet_type = "pet_type";
    private static final String qiniu_token = "qiniu_token";

    /**
     * 保存登录是否
     * @param b
     */
    public static void saveLogin(boolean b){
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putBoolean(user_islogin,b).commit();
    }

    public static boolean isLogin() {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        boolean is_login = sharedPreferences.getBoolean(user_islogin,false);
        return is_login;
    }

    /**
     * 保存 二维码
     * @param code
     */
    public static void saveQRCode(String code){
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString(pet_QRCode,code).commit();
    }

    public static String getQRCode() {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        String code = sharedPreferences.getString(pet_QRCode,"");
        return code;
    }

    /**
     * 保存 七牛token
     * @param token
     */
    public static void saveQiNiuToken(String token){
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString(qiniu_token,token).commit();
    }

    public static String getQiNiuToken() {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        String code = sharedPreferences.getString(qiniu_token,"");
        return code;
    }

    /**
     * 保存 悬赏金额
     * @param money
     */
    public static void saveMoney(String money){
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString(issue_money,money).commit();
    }

    public static String getMoney() {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        String money = sharedPreferences.getString(issue_money,"");
        return money;
    }

    /**
     * 保存 描述
     * @param describ
     */
    public static void saveDescrib(String describ){
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString(pet_describ,describ).commit();
    }

    public static String getDescrib() {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        String describ = sharedPreferences.getString(pet_describ,"");
        return describ;
    }


    /**
     * 保存 用户信息
     * @param info
     */
    public static void saveUserInfo(UserInfo info) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        if(info == null){
            editor.putString(user_name, "");
            editor.putString(user_adress, "");
            editor.putString(user_phone, "");
            editor.putString(user_pay_number, "");
            editor.putInt(user_pay_type, 0);
        }else {
            if(!TextUtils.isEmpty(info.getName())){
                editor.putString(user_name, info.getName());
            }
            if(!TextUtils.isEmpty(info.getAdress())){
                editor.putString(user_adress, info.getAdress());
            }
            if(!TextUtils.isEmpty(info.getPhone())){
                editor.putString(user_phone, info.getPhone());
            }
            if(!TextUtils.isEmpty(info.getPayNumber())){
                editor.putString(user_pay_number, info.getPayNumber());
            }
            if(info.getPayType() != 0){
                editor.putInt(user_pay_type, info.getPayType());
            }
        }
        editor.commit();//提交修改
    }

    public static UserInfo getUserInfo() {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        UserInfo info = new UserInfo();
        info.setName(sharedPreferences.getString(user_name, ""));
        info.setAdress(sharedPreferences.getString(user_adress, ""));
        info.setPhone(sharedPreferences.getString(user_phone,""));
        info.setPayNumber(sharedPreferences.getString(user_pay_number,""));
        info.setPayType(sharedPreferences.getInt(user_pay_type,0));
        return info;
    }

    /**
     * 保存 发现页 宠物信息
     * @param info
     */
    public static void savePetInfo(UserPetInfo info) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("pet", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        if(info == null){
            editor.putString(user_name, "");
            editor.putString(user_adress, "");
            editor.putString(user_phone, "");
            editor.putString(user_pay_number, "");

            editor.putString(pet_lost_time, "");
            editor.putString(pet_type, "");
            editor.putString(pet_describ, "");

        }else {
            if(!TextUtils.isEmpty(info.getPatName())){
                editor.putString(user_name, info.getPatName());
            }
            if(!TextUtils.isEmpty(info.getLoseAddress())){
                editor.putString(user_adress, info.getLoseAddress());
            }
            if(!TextUtils.isEmpty(info.getMasterPhone())){
                editor.putString(user_phone, info.getMasterPhone());
            }
            if(!TextUtils.isEmpty(info.getReward())){
                editor.putString(user_pay_number, info.getReward());
            }
            if(!TextUtils.isEmpty(info.getLoseDate())){
                editor.putString(pet_lost_time, info.getLoseDate());
            }
            if(!TextUtils.isEmpty(info.getState())){
                editor.putString(pet_type, info.getState());
            }
            if(!TextUtils.isEmpty(info.getDescrib())){
                editor.putString(pet_describ, info.getDescrib());
            }
        }
        editor.commit();//提交修改
    }

    public static UserPetInfo getPetInfo() {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("pet", Context.MODE_PRIVATE); //私有数据
        UserPetInfo info = new UserPetInfo();
        info.setPatName(sharedPreferences.getString(user_name, ""));
        info.setLoseAddress(sharedPreferences.getString(user_adress, ""));
        info.setMasterPhone(sharedPreferences.getString(user_phone,""));
        info.setReward(sharedPreferences.getString(user_pay_number,""));
        info.setLoseDate(sharedPreferences.getString(pet_lost_time,""));
        info.setState(sharedPreferences.getString(pet_type,""));
        info.setDescrib(sharedPreferences.getString(pet_describ,""));
        return info;
    }
//
//    /**
//     * 保存 图片数组
//     * @param list
//     */
//    public static void savePicsArray(ArrayList<String> list) {
//        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
//        SharedPreferences.Editor mEdit1= sharedPreferences.edit();
//        mEdit1.putInt(pic_Status_size,list.size());
//        for(int i=0;i<list.size();i++) {
//            mEdit1.remove(pic_Status_ + i);
//            mEdit1.putString(pic_Status_ + i, list.get(i));
//        }
//        mEdit1.commit();
//    }
//
//    public static ArrayList<String> loadPicsArray() {
//        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
//        ArrayList<String> list = new ArrayList<>();
//        list.clear();
//        int size = sharedPreferences.getInt(pic_Status_size, 0);
//        for(int i=0;i<size;i++) {
//            list.add(sharedPreferences.getString(pic_Status_ + i, null));
//        }
//        return list;
//    }
}
