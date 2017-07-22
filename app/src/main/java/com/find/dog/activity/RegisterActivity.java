package com.find.dog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.find.dog.R;
import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.data.UserInfo;
import com.find.dog.data.stringInfo;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText phone_edit,yzm_edit,pay_number_edit;
    private Button sure_text,yzm_text;
    private RadioGroup radioGroup;
    private RadioButton zhifubaoButton,weixinButton;
    private int type ;
    public static int REGIST_RESULT = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_layout);
        intview();
    }

    private void intview(){
        //获取实例
        radioGroup=(RadioGroup)findViewById(R.id.radioGroupID);
        zhifubaoButton=(RadioButton)findViewById(R.id.zhifubaoID);
        weixinButton=(RadioButton)findViewById(R.id.weixinID);
        //设置监听
        radioGroup.setOnCheckedChangeListener(new RadioGroupListener());

        phone_edit = (EditText) findViewById(R.id.activity_login_phone);
        yzm_edit = (EditText) findViewById(R.id.activity_login_yzm);
        pay_number_edit = (EditText) findViewById(R.id.activity_register_pay);
        yzm_text = (Button) findViewById(R.id.activity_login_yzm_text);
        sure_text = (Button) findViewById(R.id.activity_login_sure_text);
        yzm_text.setOnClickListener(this);
        sure_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_login_yzm_text:
                break;
            case R.id.activity_login_sure_text:
                String phone = phone_edit.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    ToastUtil.showTextToast(this,"手机号不能为空");
                    return;
                }
                getLoginInfo();

                break;
        }
    }

    private void getLoginInfo(){
        final String mPhone = phone_edit.getText().toString();
        final String mNumber = pay_number_edit.getText().toString();
        if(zhifubaoButton.isChecked()){
            type = UserInfo.ALIPAY;
        }else if(weixinButton.isChecked()){
            type = UserInfo.WECHATPAY;
        }
        //获取 正在悬赏宠物
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", mPhone);
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<stringInfo>(this, RetroFactory.getIstance().getStringService().getRegistInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<stringInfo>() {

                    @Override
                    public void onSuccess(stringInfo infos) {
                        Log.e("H", "getRegistInfo---->" + infos);
                        if (infos != null) {
                            UserInfo info = new UserInfo();
                            info.setPhone(mPhone);
                            info.setPayNumber(mNumber);
                            info.setPayType(type);
                            MyManger.saveUserInfo(info);
                            Intent intent = getIntent();
                            setResult(REGIST_RESULT,intent);
                            finish();
//                            Intent intent = new Intent(RegisterActivity.this,UserActivity.class);
//                            startActivity(intent);
//                            finish();
                            ToastUtil.showTextToast(RegisterActivity.this,infos.getInfo());
                        } else {
                        }
                    }

                    @Override
                    public void onFail() {
                        Log.e("H", "onFail---->");
                        ToastUtil.showTextToast(RegisterActivity.this,getResources().getString(R.string.error_net));
                    }

                });
    }

    class RadioGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId==zhifubaoButton.getId()){
                ToastUtil.showTextToast(getApplicationContext(),zhifubaoButton.getText().toString());
            }else if (checkedId==weixinButton.getId()){
                ToastUtil.showTextToast(getApplicationContext(),weixinButton.getText().toString());
            }
        }
    }
}