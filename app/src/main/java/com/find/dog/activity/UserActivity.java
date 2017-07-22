package com.find.dog.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.find.dog.R;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.ToastUtil;


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
        phone_text.setText(MyManger.getUserInfo().getPhone());
        pay_text.setText( MyManger.getUserInfo().getPayNumber() +"--"+ MyManger.getUserInfo().getPayType());
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

}