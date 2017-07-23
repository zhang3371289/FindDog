package com.find.dog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.find.dog.R;
import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.data.GetUserInfo;
import com.find.dog.data.UserInfo;
import com.find.dog.data.stringInfo;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;


public class UserActivity extends BaseActivity implements View.OnClickListener {
    private TextView phone_text,pay_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_layout);
        phone_text = (TextView) findViewById(R.id.activity_main_user_phone);
        pay_text = (TextView) findViewById(R.id.activity_main_user_pay);
        findViewById(R.id.activity_main_user_change).setOnClickListener(this);
        findViewById(R.id.activity_main_user_out).setOnClickListener(this);
        getUserInfo();
    }

    private void update(String phone,String nub){
        phone_text.setText(phone);
        pay_text.setText(nub);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_user_change:
                ToastUtil.showTextToast(this,"修改信息");
                break;
            case R.id.activity_main_user_out:
                ToastUtil.showTextToast(this,"退出登录");
                MyManger.saveUserInfo(null);
                finish();
                break;
        }
    }

    private void getUserInfo() {
        //宠物信息录入
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", "18801308610");
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<GetUserInfo>(this, RetroFactory.getIstance().getStringService().getUserInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<GetUserInfo>() {

                    @Override
                    public void onSuccess(GetUserInfo infos) {
                        Log.e("H", "getUserInfo---->" + infos);
                        if (infos != null) {
                            ToastUtil.showTextToast(getApplicationContext(),infos.toString());
                        } else {
                            update(MyManger.getUserInfo().getPhone(),MyManger.getUserInfo().getPayNumber());
                        }
                    }

                    @Override
                    public void onFail() {
                        Log.e("H", "file---->" );
                        update(MyManger.getUserInfo().getPhone(),MyManger.getUserInfo().getPayNumber());
                    }

                });
    }

}