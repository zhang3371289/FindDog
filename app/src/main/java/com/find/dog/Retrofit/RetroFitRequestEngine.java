package com.find.dog.Retrofit;

import android.database.Observable;

import com.find.dog.data.UserInfoUpdate;

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

    //完善个人信息接口 -->zzw
    @POST("user/profile")
    Call<BaseEntity<UserInfoUpdate>> updateUserInfo(@Body RequestBody route);

    //关注,取关
    @POST("focus/user")
    Call<BaseEntity<String>> focus(@Body RequestBody route);

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
