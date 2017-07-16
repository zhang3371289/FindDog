package com.find.dog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.find.dog.R;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.ToastUtil;

/**
 * Created by ZhangV on 2017/7/16.
 */

public class PayActivity extends BaseActivity implements View.OnClickListener{
    private RadioGroup radioGroup;
    private RadioButton zhifubaoButton,weixinButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_layout);
        initview();
    }

    private void initview(){
        //获取实例
        radioGroup=(RadioGroup)findViewById(R.id.radioGroupID);
        zhifubaoButton=(RadioButton)findViewById(R.id.zhifubaoID);
        weixinButton=(RadioButton)findViewById(R.id.weixinID);
        //设置监听
        radioGroup.setOnCheckedChangeListener(new RadioGroupListener());

        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.pay_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
                finish();
                break;
            case R.id.pay_button:
                Intent intent = new Intent(this,MyPetPayAfterActivity.class);
                startActivity(intent);
                break;
        }
    }

    class RadioGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId==zhifubaoButton.getId()){
                ToastUtil.showTextToast(PayActivity.this,zhifubaoButton.getText().toString());
            }else if (checkedId==weixinButton.getId()){
                ToastUtil.showTextToast(PayActivity.this,weixinButton.getText().toString());
            }
        }
    }
}
