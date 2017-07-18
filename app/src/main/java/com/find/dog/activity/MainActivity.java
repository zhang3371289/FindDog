package com.find.dog.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.find.dog.R;
import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.data.UserInfoUpdate;
import com.find.dog.fragment.CommunityFragment;
import com.find.dog.fragment.FindFragment;
import com.find.dog.fragment.PetFragment;
import com.find.dog.fragment.ScanFragment;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.NoScrollViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TabLayout tabLayout;
    private NoScrollViewPager viewPager;
    private String[] mTabTexts;
    private ArrayList<Fragment> mFragmentsList;
    private MyAdapter pageAdapter;
    private static Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        mActivity = this;
        tabLayout = (TabLayout) findViewById(R.id.home_tabs);
        viewPager = (NoScrollViewPager) findViewById(R.id.home_vp_view);
        findViewById(R.id.activity_main_user).setOnClickListener(this);
        mTabTexts = new String[]{"扫一扫", "发现", "宠物", "社区"};
        mFragmentsList = new ArrayList<>();
        mFragmentsList.add(new ScanFragment());//扫一扫
        mFragmentsList.add(new FindFragment());//发现
        mFragmentsList.add(new PetFragment());//宠物
        mFragmentsList.add(new CommunityFragment());//社区
        for (int i = 0; i < mTabTexts.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mTabTexts[i]));
        }
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        pageAdapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setNeedScroll(false);//取消左右滑动
//        setTabLayoutCanClick(false);//不可点击Tab页
        onPermissionRequests(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnBooleanListener() {
            @Override
            public void onClick(boolean bln) {
                if (bln) {

                } else {
                    Toast.makeText(MainActivity.this, "文件读写或无法正常使用", Toast.LENGTH_SHORT).show();
                }
            }
        });

        get();
    }

    private void get(){
        //获取 完善个人信息数据
        Map<String, String> map = new HashMap<>();
//        map.put("authtoken", "8fd9El4eLyKCsIDgFCrWRve59sKCo2lF45IMrS2O6Ns5ZKd%2F4g0g7G8%2B0g");
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<ArrayList<UserInfoUpdate>>(this, RetroFactory.getIstance().getStringService().updateUserInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<ArrayList<UserInfoUpdate>>() {

                    @Override
                    public void onSuccess(ArrayList<UserInfoUpdate> infos) {
                        Log.e("H", "updateUserInfo---->" + infos);
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
        }
    }

    //自由控制TabLayout是否可以点击：
    public void setTabLayoutCanClick(boolean canClick) {
        LinearLayout tabStrip = (LinearLayout) tabLayout.getChildAt(0);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            View tabView = tabStrip.getChildAt(i);
            if (tabView != null) {
                tabView.setClickable(canClick);
            }
        }
    }

    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTexts[position];
        }
    }

    ////////////////////   7.0权限申请  相机+存储///////////////////////////
    private static OnBooleanListener onPermissionListener;//权限监听

    public static void onPermissionRequests(String permission, OnBooleanListener onBooleanListener) {
        onPermissionListener = onBooleanListener;
        if (ContextCompat.checkSelfPermission(mActivity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                    Manifest.permission.READ_CONTACTS)) {
                //权限已有
                onPermissionListener.onClick(true);
            } else {
                //没有权限，申请一下
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{permission},
                        1);
            }
        } else {
            onPermissionListener.onClick(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限通过
                if (onPermissionListener != null) {
                    onPermissionListener.onClick(true);
                }
            } else {
                //权限拒绝
                if (onPermissionListener != null) {
                    onPermissionListener.onClick(false);
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public interface OnBooleanListener {
        void onClick(boolean bln);
    }

    ////////////////////   7.0权限申请 结束 ///////////////////////////

}