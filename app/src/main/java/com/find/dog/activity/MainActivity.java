package com.find.dog.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;


public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static Activity mActivity;
    private static TextView locationTextView;
    private static int showCount;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ArrayList<rewardingInfo> mList = new ArrayList<>();
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
                    showCount = 1;
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
                            mList = infos;
                            mAdapter.notifyDataSetChanged();
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
                if(TextUtils.isEmpty(MyManger.getUserInfo().getPhone())){
                    intent = new Intent(this,LoginActivity.class);
                }else{
                    intent = new Intent(this,UserActivity.class);
                    intent.putExtra("phone", MyManger.getUserInfo());
                }
                startActivity(intent);
                break;
            case R.id.activity_main_tab1:
                ToastUtil.showTextToast(mActivity,"扫一扫");
//                //打开扫描界面扫描条形码或二维码
//                onPermissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
//                    @Override
//                    public void onClick(boolean bln) {
//                        if (bln) {
//                            Intent openCameraIntent = new Intent(mActivity, CaptureActivity.class);
//                            startActivityForResult(openCameraIntent, 0);
//                        } else {
//                            Toast.makeText(mActivity, "未打开相机权限", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                startActivity(new Intent(this, UpLoadActivity.class));
                break;
            case R.id.activity_main_tab2:
                ToastUtil.showTextToast(mActivity,"发现");
                startActivity(new Intent(this, FindActivity.class));
                break;
            case R.id.activity_main_tab3:
                ToastUtil.showTextToast(mActivity,"宠物");
                startActivity(new Intent(this, MyPetActivity.class));
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
                    locationTextView.setText(showCount+"\t"+result);
                    showCount++;
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
            rewardingInfo info = mList.get(position);
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

}