package com.find.dog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.find.dog.R;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.ToastUtil;


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
                MyManger.saveUserInfo(phone);
                Intent intent = new Intent(this,UserActivity.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
                break;
        }
    }
}