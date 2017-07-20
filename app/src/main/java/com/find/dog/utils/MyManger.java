package com.find.dog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.find.dog.main.MyApplication;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangzhongwei on 2017/6/29.
 */

public class MyManger {
    public static final String BASE = "http://zhaogou.applinzi.com/";//正式环境

    /**
     * 保存手机号
     * @param phone
     */
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
