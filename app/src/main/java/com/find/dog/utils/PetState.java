package com.find.dog.utils;

/**
 * Created by zhangzhongwei on 2017/7/31.
 */

public class PetState {
    public static String getState(String state){
        String returnState = state;
        if("normal".equals(state)){
            returnState = "正常";
        }else if("lose".equals(state)){
            returnState = "丢失";
        }else if("confirming".equals(state)){
            returnState = "确认中";
        }
        return returnState;
    }
}
