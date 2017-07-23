package com.find.dog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.find.dog.data.UserInfo;
import com.find.dog.main.MyApplication;

import java.util.ArrayList;

/**
 * Created by zhangzhongwei on 2017/6/29.
 */

public class MyManger {
    public static final String BASE = "http://zhaogou.applinzi.com/";//正式环境


    public static boolean isLogin() {
        if (TextUtils.isEmpty(getUserInfo().getPhone())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 保存 用户信息
     * @param info
     */
    public static void saveUserInfo(UserInfo info) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        if(info == null){
            editor.putString("name", "");
            editor.putString("adress", "");
            editor.putString("phone", "");
            editor.putString("pay_number", "");
            editor.putInt("pay_type", 0);
        }else {
            if(!TextUtils.isEmpty(info.getName())){
                editor.putString("name", info.getName());
            }
            if(!TextUtils.isEmpty(info.getAdress())){
                editor.putString("adress", info.getAdress());
            }
            if(!TextUtils.isEmpty(info.getPhone())){
                editor.putString("phone", info.getPhone());
            }
            if(!TextUtils.isEmpty(info.getPayNumber())){
                editor.putString("pay_number", info.getPayNumber());
            }
            if(info.getPayType() != 0){
                editor.putInt("pay_type", info.getPayType());
            }
        }
        editor.commit();//提交修改
    }

    public static UserInfo getUserInfo() {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        UserInfo info = new UserInfo();
        info.setName(sharedPreferences.getString("name", ""));
        info.setAdress(sharedPreferences.getString("adress", ""));
        info.setPhone(sharedPreferences.getString("phone",""));
        info.setPayNumber(sharedPreferences.getString("pay_number",""));
        info.setPayType(sharedPreferences.getInt("pay_type",0));
        return info;
    }

    /**
     * 保存 图片数组
     * @param list
     */
    public static void savePicsArray(ArrayList<String> list) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor mEdit1= sharedPreferences.edit();
        mEdit1.putInt("Status_size",list.size());
        for(int i=0;i<list.size();i++) {
            mEdit1.remove("Status_" + i);
            mEdit1.putString("Status_" + i, list.get(i));
        }
        mEdit1.commit();
    }

    public static ArrayList<String> loadPicsArray() {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        ArrayList<String> list = new ArrayList<>();
        list.clear();
        int size = sharedPreferences.getInt("Status_size", 0);
        for(int i=0;i<size;i++) {
            list.add(sharedPreferences.getString("Status_" + i, null));
        }
        return list;
    }
}
