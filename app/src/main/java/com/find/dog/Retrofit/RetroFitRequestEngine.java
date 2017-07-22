package com.find.dog.Retrofit;

import android.database.Observable;

import com.find.dog.data.rewardingInfo;
import com.find.dog.data.stringInfo;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by czz on 2017/4/15.
 * 所有的接口信息都在这里配置
 */

public interface RetroFitRequestEngine {

    //获取正在悬赏的宠物(XX省XX市XX区/镇)
    @POST("getinfo_reward.php")
    Call<BaseEntity<ArrayList<rewardingInfo>>> getRewardInfo(@Body RequestBody route);

    //登录
    @POST("login.php")
    Call<BaseEntity<stringInfo>> getLoginInfo(@Body RequestBody route);

    //注册
    @POST("regist_user.php")
    Call<BaseEntity<stringInfo>> getRegistInfo(@Body RequestBody route);
    //上传图片和描述
    @Multipart
    @POST("url")
    Observable<String> uploadUserFile(
            @Part("fileName") RequestBody description,
            @Part("file\"; filename=\"image.png\"") RequestBody img
    );

    @Multipart
    @POST("url")
    Observable<String> uploadUserFileAndId(
            @Part("describe") String describe,
            @Part("type") String type,
            @Part("userid") String userid,
            @Part("fileName") RequestBody description,
            @Part("file\"; filename=\"image.png\"") RequestBody img
    );

}
