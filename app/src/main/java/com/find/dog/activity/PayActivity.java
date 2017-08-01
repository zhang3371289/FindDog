package com.find.dog.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.find.dog.R;
import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.data.stringInfo;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.QINiuUtil;
import com.find.dog.utils.ToastUtil;
import com.find.dog.utils.YKUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by ZhangV on 2017/7/16.
 */

public class PayActivity extends BaseActivity implements View.OnClickListener{
    private RadioGroup radioGroup;
    private RadioButton zhifubaoButton,weixinButton;
    private String money;
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
                uploadPic();
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

    /**
     * 七牛 上传图片
     */
    private void uploadPic(){
        if(!YKUtil.isNetworkAvailable()){
            ToastUtil.showTextToast(this,getResources().getString(R.string.intent_no));
            return;
        }
        QINiuUtil.getInstance().uploadPic(this,MyManger.loadPicsArray(), new QINiuUtil.Callback() {
            @Override
            public void callback(boolean isOk,Map<String, String> pic_map) {
                if(isOk){
                    sendIssue(pic_map);
                }else{
                    QINiuUtil.dismissDialog();
                }
            }
        });
    }

    private void sendIssue(final Map<String, String> pic_map){
        //发布悬赏
        final Map<String, String> map = new HashMap<>();
        map.put("userPhone", MyManger.getPetInfo().getMasterPhone());
        map.put("alipay", "");
        map.put("wechatpay", "");
        map.put("loseAddress", MyManger.getPetInfo().getLoseAddress());
        map.put("reward", MyManger.getPetInfo().getReward());
        map.put("describ", MyManger.getDescrib());
        map.put("2dCode", MyManger.getQRCode());
        map.putAll(pic_map);

        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<stringInfo>(this, RetroFactory.getIstance().getStringService().sendRewardInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<stringInfo>() {

                    @Override
                    public void onSuccess(stringInfo result) {
                        QINiuUtil.dismissDialog();
                        if (!TextUtils.isEmpty(result.getInfo())) {
                            // 将Map value 转化为List
                            ArrayList<String> mapValuesList = new ArrayList<String>(pic_map.values());
                            MyManger.savePicsArray(mapValuesList);
//                            Intent intent = new Intent(PayActivity.this,MyPetPayAfterActivity.class);
//                            startActivity(intent);
                            finish();
                        } else {
                        }
                    }

                    @Override
                    public void onFail() {
                        QINiuUtil.dismissDialog();
                    }

                });
    }
}
