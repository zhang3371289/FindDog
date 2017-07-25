package com.find.dog.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.find.dog.R;
import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.data.GetUserInfo;
import com.find.dog.data.UserInfo;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;


public class UserActivity extends BaseActivity implements View.OnClickListener {
    private TextView phone_text,pay_type,pay_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_layout);
        phone_text = (TextView) findViewById(R.id.activity_main_user_phone);
        pay_type = (TextView) findViewById(R.id.activity_main_user_pay_type);
        pay_text = (TextView) findViewById(R.id.activity_main_user_pay);
        findViewById(R.id.activity_main_user_change).setOnClickListener(this);
        findViewById(R.id.activity_main_user_out).setOnClickListener(this);
        getUserInfo();
    }

    private void update(GetUserInfo infos){
        UserInfo infoBean = new UserInfo();
        phone_text.setText(infos.getUserPhone());
        if(!TextUtils.isEmpty(infos.getWechatpay())){
            pay_type.setText("微信账号:");
            pay_text.setText(infos.getWechatpay());
            infoBean.setName(infos.getWechatpay());
            infoBean.setPayType(UserInfo.WECHATPAY);
        }else{
            pay_type.setText("支付宝账号:");
            pay_text.setText(infos.getAlipay());
            infoBean.setName(infos.getAlipay());
            infoBean.setPayType(UserInfo.ALIPAY);
        }
        infoBean.setAdress(infos.getHomeAddress());
        infoBean.setPhone(infos.getUserPhone());
        MyManger.saveUserInfo(infoBean);

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
        Log.e("H", "getUserInfo---->");
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
//                            ToastUtil.showTextToast(getApplicationContext(),infos.toString());
                            update(infos);
                        } else {
                        }
                    }

                    @Override
                    public void onFail() {
                        Log.e("H", "file---->" );
                    }

                });
    }


}