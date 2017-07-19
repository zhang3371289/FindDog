package com.find.dog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.find.dog.R;
import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.data.stringInfo;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText phone_edit,yzm_edit;
    private TextView sure_text,yzm_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_layout);
        phone_edit = (EditText) findViewById(R.id.activity_login_phone);
        yzm_edit = (EditText) findViewById(R.id.activity_login_yzm);
        yzm_text = (TextView) findViewById(R.id.activity_login_yzm_text);
        sure_text = (TextView) findViewById(R.id.activity_login_sure_text);
        yzm_text.setOnClickListener(this);
        sure_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_login_yzm_text:
                ToastUtil.showTextToast(this,yzm_edit.getText().toString());
                break;
            case R.id.activity_login_sure_text:
                String phone = phone_edit.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    ToastUtil.showTextToast(this,"手机号不能为空");
                    return;
                }
                getLoginInfo(phone);

                break;
        }
    }

    private void getLoginInfo(final String mPhone){
        //获取 正在悬赏宠物
        Map<String, String> map = new HashMap<>();
        map.put("loseAddress", "北京市");
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<stringInfo>(this, RetroFactory.getIstance().getStringService().getLoginInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<stringInfo>() {

                    @Override
                    public void onSuccess(stringInfo infos) {
                        Log.e("H", "getLoginInfo---->" + infos);
                        if (infos != null) {
//                            updateUI(infos);

                            MyManger.saveUserInfo(mPhone);
                            Intent intent = new Intent(LoginActivity.this,UserActivity.class);
                            intent.putExtra("phone",mPhone);
                            startActivity(intent);
                            finish();


                        } else {
                        }
                    }

                    @Override
                    public void onFail() {
                        Log.e("H", "onFail---->");
                    }

                });
    }
}