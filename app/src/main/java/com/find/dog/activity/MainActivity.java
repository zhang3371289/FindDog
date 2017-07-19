package com.find.dog.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.find.dog.R;
import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.data.rewardingInfo;
import com.find.dog.main.BaseActivity;
import com.find.dog.main.MyApplication;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.ToastUtil;
import com.google.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static Activity mActivity;
    private static TextView location_text;
    private static int showCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        initview();
        getRewardInfo();
    }
    private void initview(){
        mActivity = this;
        findViewById(R.id.activity_main_user).setOnClickListener(this);
        findViewById(R.id.activity_main_tab1).setOnClickListener(this);
        findViewById(R.id.activity_main_tab2).setOnClickListener(this);
        findViewById(R.id.activity_main_tab3).setOnClickListener(this);
        findViewById(R.id.activity_main_tab4).setOnClickListener(this);
        location_text = (TextView) findViewById(R.id.activity_main_location);
        onPermissionRequests(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnBooleanListener() {
            @Override
            public void onClick(boolean bln) {
                if (bln) {

                } else {
                    Toast.makeText(MainActivity.this, "文件读写或无法正常使用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        onPermissionRequests(Manifest.permission.ACCESS_FINE_LOCATION, new BaseActivity.OnBooleanListener() {
            @Override
            public void onClick(boolean bln) {
                if (bln) {
                    showCount = 1;
                    MyApplication.getInstance().mLocationClient.start();
                } else {
                    Toast.makeText(MyApplication.getInstance(), "文件读写或无法正常使用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getRewardInfo(){
        //获取 正在悬赏宠物
        Map<String, String> map = new HashMap<>();
        map.put("loseAddress", "北京市");
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<ArrayList<rewardingInfo>>(this, RetroFactory.getIstance().getStringService().getRewardInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<ArrayList<rewardingInfo>>() {

                    @Override
                    public void onSuccess(ArrayList<rewardingInfo> infos) {
                        Log.e("H", "rewardingInfo---->" + infos);
                        if (infos != null) {
//                            updateUI(infos);
                        } else {
                        }
                    }

                    @Override
                    public void onFail() {
                        Log.e("H", "onFail---->");
                    }

                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (data != null) {
            ToastUtil.showTextToast(this,data.getStringExtra("result"));
            if(resultCode == 300){
                startActivity(new Intent(this, FindActivity.class));
            }else {
                startActivity(new Intent(this, UpLoadActivity.class));
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().mLocationClient.stop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_user:
                Intent intent;
                if(TextUtils.isEmpty(MyManger.getUserInfo())){
                    intent = new Intent(this,LoginActivity.class);
                }else{
                    intent = new Intent(this,UserActivity.class);
                    intent.putExtra("phone", MyManger.getUserInfo());
                }
                startActivity(intent);
                break;
            case R.id.activity_main_tab1:
                ToastUtil.showTextToast(mActivity,"扫一扫");
                //打开扫描界面扫描条形码或二维码
                onPermissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
                    @Override
                    public void onClick(boolean bln) {
                        if (bln) {
                            Intent openCameraIntent = new Intent(mActivity, CaptureActivity.class);
                            startActivityForResult(openCameraIntent, 0);
                        } else {
                            Toast.makeText(mActivity, "未打开相机权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.activity_main_tab2:
                ToastUtil.showTextToast(mActivity,"发现");
                break;
            case R.id.activity_main_tab3:
                ToastUtil.showTextToast(mActivity,"宠物");
                break;
            case R.id.activity_main_tab4:
                ToastUtil.showTextToast(mActivity,"社区");
                break;
        }
    }

    public static void setLocation(final String result){
        if(location_text!=null){
            location_text.post(new Runnable() {
                @Override
                public void run() {
                    location_text.setText(showCount+"\t"+result);
                    showCount++;
                }
            });
        }
    }

}