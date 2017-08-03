package com.find.dog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.find.dog.R;
import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.data.UserInfo;
import com.find.dog.data.stringInfo;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.PhoneUtil;
import com.find.dog.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;


public class ChangeUserActivity extends BaseActivity implements View.OnClickListener {
    private EditText phone_edit,yzm_edit,pay_number_edit,adress_edit;
    private LinearLayout adress_layout;
    private Button sure_text,yzm_text;
    private TextView title_text;
    private RadioGroup radioGroup;
    private RadioButton zhifubaoButton,weixinButton;
    private int type ;
    public static final int REGIST_RESULT = 101;
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
        title_text = (TextView) findViewById(R.id.title_textView);
        //设置监听
        radioGroup.setOnCheckedChangeListener(new RadioGroupListener());

        phone_edit = (EditText) findViewById(R.id.activity_login_phone);
        yzm_edit = (EditText) findViewById(R.id.activity_login_yzm);
        pay_number_edit = (EditText) findViewById(R.id.activity_register_pay);
        adress_edit = (EditText) findViewById(R.id.activity_login_adress);
        adress_layout = (LinearLayout) findViewById(R.id.activity_login_adress_layout);
        yzm_text = (Button) findViewById(R.id.activity_login_yzm_text);
        sure_text = (Button) findViewById(R.id.activity_login_sure_text);
        sure_text.setText("修改");
        title_text.setText("修改信息");
        yzm_text.setOnClickListener(this);
        sure_text.setOnClickListener(this);
        adress_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_login_yzm_text:
                PhoneUtil.getInstance().getSmsCode(phone_edit.getText().toString(),yzm_text);
                break;
            case R.id.activity_login_sure_text:
                String phone = phone_edit.getText().toString();
                String yzmCode = yzm_edit.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    ToastUtil.showTextToast(this,"手机号不能为空");
                    return;
                }

                PhoneUtil.getInstance().checkSmsCode(phone, yzmCode, new PhoneUtil.PhoneCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        alterUserInfo();
                    }
                });

                break;
        }
    }

    private void alterUserInfo(){
        final String mPhone = phone_edit.getText().toString();
        final String mNumber = pay_number_edit.getText().toString();
        //获取 正在悬赏宠物
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", MyManger.getUserInfo().getPhone());
        map.put("newPhone", mPhone);
        map.put("homeAddress", adress_edit.getText().toString());
        if(zhifubaoButton.isChecked()){
            type = UserInfo.ALIPAY;
            map.put("alipay",mNumber);
        }else if(weixinButton.isChecked()){
            type = UserInfo.WECHATPAY;
            map.put("wechatpay",mNumber);
        }
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<stringInfo>(this, RetroFactory.getIstance().getStringService().alterUserInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<stringInfo>() {

                    @Override
                    public void onSuccess(stringInfo infos) {
                        if (!TextUtils.isEmpty(infos.getInfo())) {
                            UserInfo info = new UserInfo();
                            info.setPhone(mPhone);
                            info.setPayNumber(mNumber);
                            info.setPayType(type);
                            MyManger.saveUserInfo(info);
                            MyManger.saveLogin(true);
                            Intent intent = getIntent();
                            setResult(REGIST_RESULT,intent);
                            finish();
//                            Intent intent = new Intent(RegisterActivity.this,UserActivity.class);
//                            startActivity(intent);
//                            finish();
                            ToastUtil.showTextToast(ChangeUserActivity.this,infos.getInfo().toString());
                        } else {
                            ToastUtil.showTextToast(getApplicationContext(),infos.getErro());
                        }
                    }

                    @Override
                    public void onFail() {
                        ToastUtil.showTextToast(ChangeUserActivity.this,getResources().getString(R.string.error_net));
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