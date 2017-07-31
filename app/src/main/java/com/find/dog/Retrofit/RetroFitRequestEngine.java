package com.find.dog.Retrofit;

import android.database.Observable;

import com.find.dog.data.GetUserInfo;
import com.find.dog.data.QiNiuInfo;
import com.find.dog.data.UserPetInfo;
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

    //登录
    @POST("login.php")
    Call<BaseEntity<stringInfo>> getLoginInfo(@Body RequestBody route);

    //注册
    @POST("regist_user.php")
    Call<BaseEntity<stringInfo>> getRegistInfo(@Body RequestBody route);

    //修改user信息
    @POST("alter_user.php")
    Call<BaseEntity<stringInfo>> alterUserInfo(@Body RequestBody route);

    //发送心跳(客户端发送)登陆后开始间隔发送
    @POST("alive.php")
    Call<BaseEntity<stringInfo>> sendAliveInfo(@Body RequestBody route);

    //宠物信息录入
    @POST("regist_pat.php")
    Call<BaseEntity<stringInfo>> getRegistPetInfo(@Body RequestBody route);

    //获取用户信息
    @POST("getinfo_user.php")
    Call<BaseEntity<GetUserInfo>> getUserInfo(@Body RequestBody route);

    //获取用户所有宠物
    @POST("getinfo_userpats.php")
    Call<BaseEntity<ArrayList<UserPetInfo>>> getUserAllPetInfo(@Body RequestBody route);

    //获取单个宠物
    @POST("getinfo_pat.php")
    Call<BaseEntity<ArrayList<UserPetInfo>>> getUserPetInfo(@Body RequestBody route);

    //获取已找到的所有宠物
    @POST("getinfo_backpat.php")
    Call<BaseEntity<stringInfo>> getUserBackPetInfo(@Body RequestBody route);

    //获取正在悬赏的宠物(XX省XX市XX区/镇)
    @POST("getinfo_reward.php")
    Call<BaseEntity<ArrayList<UserPetInfo>>> getRewardInfo(@Body RequestBody route);

    //获取“发现”数据
    @POST("getfindinfo.php")
    Call<BaseEntity<ArrayList<UserPetInfo>>> getFindInfo(@Body RequestBody route);

    //获取 扫一扫 登录后 “发现”数据
    @POST("getfindinfo_login_sao.php")
    Call<BaseEntity<ArrayList<UserPetInfo>>> getFindIsLoginInfo(@Body RequestBody route);

    //改变宠物状态（确认中） lose->confirming
    @POST("state_loseToConfirming.php")
    Call<BaseEntity<stringInfo>> changeLoseToConfirmingState(@Body RequestBody route);

    //改变宠物状态(取消悬赏) lose->normal
    @POST("state_loseToNormal.php")
    Call<BaseEntity<stringInfo>> changeLoseToNormalState(@Body RequestBody route);

    //改变宠物状态（确认失败，继续悬赏） confirming->lose
    @POST("state_confirmingToLose.php")
    Call<BaseEntity<stringInfo>> changeConfirmingToLoseState(@Body RequestBody route);

    //改变宠物状态（宠物找回） confirming->normal
    @POST("state_confirmingToNormal.php")
    Call<BaseEntity<stringInfo>> changeConfirmingToNormalState(@Body RequestBody route);

    //发布悬赏
    @POST("release_reward.php")
    Call<BaseEntity<stringInfo>> sendRewardInfo(@Body RequestBody route);

    //改变宠物信息
    @POST("alter_pat.php")
    Call<BaseEntity<stringInfo>> changePetInfo(@Body RequestBody route);

    //删除宠物
    @POST("delet_pat.php")
    Call<BaseEntity<stringInfo>> deletePetInfo(@Body RequestBody route);

    //获取七牛token
    @POST("phpsdk/examples/upload_token.php")
    Call<BaseEntity<QiNiuInfo>> getTokenInfo(@Body RequestBody route);

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
