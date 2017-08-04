package com.find.dog.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.find.dog.R;
import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.data.QiNiuInfo;
import com.find.dog.data.UserPetInfo;
import com.find.dog.main.BaseActivity;
import com.find.dog.main.MyApplication;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.ToastUtil;
import com.find.dog.utils.YKUtil;
import com.google.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static Activity mActivity;
    private static TextView locationTextView;
    private RecyclerView mRecyclerView;
    private static MyAdapter mAdapter;
    private static ArrayList<UserPetInfo> mList = new ArrayList<>();
    private static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        mContext = this;
        initview();
        getTokenInfo();
    }
    private void initview(){
        mActivity = this;
        findViewById(R.id.activity_main_user).setOnClickListener(this);
        findViewById(R.id.activity_main_tab1).setOnClickListener(this);
        findViewById(R.id.activity_main_tab2).setOnClickListener(this);
        findViewById(R.id.activity_main_tab3).setOnClickListener(this);
        findViewById(R.id.activity_main_tab4).setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycleview);
        locationTextView = (TextView) findViewById(R.id.activity_main_location);
        //创建默认的线性LayoutManager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        onPermissionRequests(Manifest.permission.ACCESS_FINE_LOCATION, new BaseActivity.OnBooleanListener() {
            @Override
            public void onClick(boolean bln) {
                if (bln) {
                    MyApplication.getInstance().mLocationClient.start();
                } else {
                    Toast.makeText(MyApplication.getInstance(), "未获取位置权限", Toast.LENGTH_SHORT).show();
                }
                onPermissionRequests(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnBooleanListener() {
                    @Override
                    public void onClick(boolean bln) {
                        if (bln) {

                        } else {
                            Toast.makeText(MainActivity.this, "文件读写或无法正常使用", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private static void getRewardInfo(String location){
        //获取 正在悬赏宠物
        Map<String, String> map = new HashMap<>();
        map.put("loseAddress", location);
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<ArrayList<UserPetInfo>>(mContext, RetroFactory.getIstance().getStringService().getRewardInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<ArrayList<UserPetInfo>>() {

                    @Override
                    public void onSuccess(ArrayList<UserPetInfo> infos) {
                        if (infos != null) {
//                            updateUI(infos);
                            mList = infos;
                            mAdapter.notifyDataSetChanged();
                        } else {
                        }
                    }

                    @Override
                    public void onFail() {
                    }

                });
    }

    private void getTokenInfo(){
        //获取 七牛token
        Map<String, String> map = new HashMap<>();
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<QiNiuInfo>(mContext, RetroFactory.getIstance().getStringService().getTokenInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<QiNiuInfo>() {

                    @Override
                    public void onSuccess(QiNiuInfo info) {
//                        Log.e("H", "getTokenInfo---->" + info);
                        if (!TextUtils.isEmpty(info.getRes())) {
                            MyManger.saveQiNiuToken(info.getRes());
                        } else {
                        }
                    }

                    @Override
                    public void onFail() {
                    }

                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (data != null) {
            String result = data.getStringExtra("result");
            MyManger.saveQRCode(result);
            getPetInfo();
//            if(MyManger.isLogin()){
//                getIsLoginPetInfo();
//            }else {
//                getPetInfo();
//            }
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
                if(TextUtils.isEmpty(MyManger.getUserInfo().getPhone())){
                    intent = new Intent(this,LoginActivity.class);
                }else{
                    intent = new Intent(this,UserActivity.class);
                }
                startActivity(intent);

                break;
            case R.id.activity_main_tab1:
                if(!YKUtil.isNetworkAvailable()){
                    ToastUtil.showTextToast(mContext,getResources().getString(R.string.intent_no));
                    return;
                }
                //打开扫描界面扫描条形码或二维码
                MainActivity.onPermissionRequests(Manifest.permission.CAMERA, new MainActivity.OnBooleanListener() {
                    @Override
                    public void onClick(boolean bln) {
                        if (bln) {
                            Intent openCameraIntent = new Intent(getApplicationContext(), CaptureActivity.class);
                            startActivityForResult(openCameraIntent, CaptureActivity.CAMERA_RESULT);
                        } else {
                            Toast.makeText(getApplicationContext(), "未打开相机权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.activity_main_tab2:
//                ToastUtil.showTextToast(mActivity,"发现");
                startActivity(new Intent(this, FindActivity.class));
                break;
            case R.id.activity_main_tab3:
//                ToastUtil.showTextToast(mActivity,"宠物");
                Intent intent1 = new Intent(this, FindActivity.class);
                intent1.putExtra("isMyPet",true);
                startActivity(intent1);
                break;
            case R.id.activity_main_tab4:
                ToastUtil.showTextToast(mActivity,"社区");
                break;
        }
    }

    public static void setLocation(final String result){
        if(locationTextView !=null){
            locationTextView.post(new Runnable() {
                @Override
                public void run() {
                    locationTextView.setText(result);
                    getRewardInfo(result);
                }
            });
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_main_layout_item,viewGroup,false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }
        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            UserPetInfo info = mList.get(position);
            viewHolder.mName.setText(info.getPatName());
            viewHolder.mMoney.setText(info.getReward());
            viewHolder.mType.setText(info.getState());
        }
        //获取数据的数量
        @Override
        public int getItemCount() {
            return mList.size();
        }
        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mName,mType,mMoney;
            public ImageView mImg;
            public ViewHolder(View view){
                super(view);
                mName = (TextView) view.findViewById(R.id.main_item_name);
                mMoney = (TextView) view.findViewById(R.id.main_item_money);
                mType = (TextView) view.findViewById(R.id.main_item_type);
                mImg = (ImageView) view.findViewById(R.id.main_item_img);
            }
        }
    }


    /**
     *
     * 未登录情况下
     */
    private void getPetInfo(){
        //获取单个宠物
        final Map<String, String> map = new HashMap<>();
        map.put("2dCode", MyManger.getQRCode());
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<ArrayList<UserPetInfo>>(this, RetroFactory.getIstance().getStringService().getUserPetInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<ArrayList<UserPetInfo>>() {

                    @Override
                    public void onSuccess(ArrayList<UserPetInfo> result) {
                        if (result != null && result.size()>0) {
                            if("lose".equals(result.get(0).getState())){
                                Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                                intent.putExtra("objectList", result);
                                startActivity(intent);
                            }else if("-1".equals(result.get(0).getId())){
                                startActivity(new Intent(getApplicationContext(), UpLoadActivity.class));
                            }else{
                                ToastUtil.showTextToast(getApplicationContext(),"你好，我是"+result.get(0).getPatName());
                            }
                        }
                    }

                    @Override
                    public void onFail() {
                        ToastUtil.showTextToast(getApplicationContext(),"二维码不合法");
                    }

                });
    }

//    private void getIsLoginPetInfo() {
//        //获取“发现”数据 登录情况下
//        Map<String, String> map = new HashMap<>();
//        map.put("userPhone", MyManger.getUserInfo().getPhone());
//        map.put("2dCode", MyManger.getQRCode());
//        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
//        new RetroFitUtil<ArrayList<UserPetInfo>>(this, RetroFactory.getIstance().getStringService().getFindIsLoginInfo(requestBody))
//                .request(new RetroFitUtil.ResponseListener<ArrayList<UserPetInfo>>() {
//
//                    @Override
//                    public void onSuccess(ArrayList<UserPetInfo> result) {
////						Log.e("H", "getFindInfo---->" + infos);
//                        if (result != null && result.size()>0) {
//                            Intent intent = new Intent(getApplicationContext(), FindActivity.class);
//                            intent.putExtra("objectList", result);
//                            startActivity(intent);
//                        }
//                    }
//                    @Override
//                    public void onFail() {
////						Log.e("H", "onFail---->");
//                    }
//
//                });
//    }

}