package com.find.dog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.find.dog.main.MyApplication;

/**
 * Created by zhangzhongwei on 2017/6/29.
 */

public class MyManger {
    public static final String BASE = "http://fujun.yoka.com/api/";//正式环境

    public static void saveUserInfo(String phone) {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("phone", phone);
        editor.commit();//提交修改
    }

    public static String getUserInfo() {
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE); //私有数据
        String str = sharedPreferences.getString("phone", "");
        return str;
    }

    public static boolean isLogin() {
        if (TextUtils.isEmpty(getUserInfo())) {
            return false;
        } else {
            return true;
        }
    }
}
