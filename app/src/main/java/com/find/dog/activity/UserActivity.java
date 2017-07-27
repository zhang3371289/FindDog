package com.find.dog.activity;

import android.content.Intent;
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
        phone_text.setText(MyManger.getUserInfo().getPhone());
        getUserInfo();
    }

    private void update(GetUserInfo infos){
        UserInfo info = new UserInfo();
        phone_text.setText(infos.getUserPhone());
        if(!TextUtils.isEmpty(infos.getWechatpay())){
            pay_type.setText("微信账号:");
            pay_text.setText(infos.getWechatpay());
            info.setPayNumber(infos.getWechatpay());
            info.setPayType(UserInfo.WECHATPAY);
        }else{
            pay_type.setText("支付宝账号:");
            pay_text.setText(infos.getAlipay());
            info.setPayNumber(infos.getAlipay());
            info.setPayType(UserInfo.ALIPAY);
        }
        info.setPhone(infos.getUserPhone());
        info.setAdress(infos.getHomeAddress());
        MyManger.saveUserInfo(info);
        MyManger.saveLogin(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_user_change:
                Intent intent = new Intent(this,ChangeUserActivity.class);
                startActivityForResult(intent,ChangeUserActivity.REGIST_RESULT);
                break;
            case R.id.activity_main_user_out:
                ToastUtil.showTextToast(this,"退出登录");
                MyManger.saveUserInfo(null);
                MyManger.saveLogin(false);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case ChangeUserActivity.REGIST_RESULT:
                getUserInfo();
                break;
        }
    }




    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        Log.e("H", "getUserInfo---->");
        String phone = getIntent().getStringExtra("phone");
        if(TextUtils.isEmpty(phone)){
            phone = MyManger.getUserInfo().getPhone();
        }
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", phone);
//        map.put("userPhone", "18801308610");
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