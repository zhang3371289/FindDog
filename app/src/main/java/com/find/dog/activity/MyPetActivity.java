package com.find.dog.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.find.dog.R;
import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.adapter.PetFooterAdapter;
import com.find.dog.adapter.PetTopAdapter;
import com.find.dog.data.UserPetInfo;
import com.find.dog.data.stringInfo;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.PetState;
import com.find.dog.utils.ToastUtil;
import com.find.dog.utils.YKUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by zhangzhongwei on 2017/7/11.
 * 发现页
 */
public class MyPetActivity extends BaseActivity implements View.OnClickListener {
    private ListView mListView;
    private RecyclerView mTopRV, mFootRV;
    private Context mContext;
    private PetFooterAdapter mFooterAdapter;
    private static int selectPosition;
    private ArrayList<String> mPicList = new ArrayList<String>();//图片路径集合
    private TextView name_text, type_text, phone_text, adress_text, title, describ_text, raward_text, tiem_text, zhuzhi_title;
    private Button mButton;
    private LinearLayout mSureLayout, mLostLayout, mNormalState, lose_text_layout;
    private PetTopAdapter mTopAdapter;
    private ArrayList<UserPetInfo> mPetsList = new ArrayList<>();
    private boolean isFromQRCode;
    private View footerView;
    private String MY_PET = "我的宠物", FIND = "发现", GIVE_UP_CONTACT = "放弃联系";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypet_layout);
        intview();
        selectPosition = 0;
        mPetsList = (ArrayList) getIntent().getSerializableExtra("objectList");
        if (mPetsList == null) {// 点击发现 宠物 信息
            isFromQRCode = false;
            if (getIntent().getBooleanExtra("isMyPet", false)) {//宠物页面
                title.setText(MY_PET);
            } else if (getIntent().getBooleanExtra("isFormUpLoad", false)) {//上传页面
                title.setText(MY_PET);
                getUserAllPetInfo(true);
                mTopRV.setVisibility(View.GONE);
            } else {//发现页面
                getUserPetInfo();
            }
        } else { // 二维码传入信息
            isFromQRCode = true;
            getData(0);
            mTopAdapter.updateData(mPetsList);
            mFooterAdapter.notifyDataSetChanged();
            mListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MY_PET.equals(title.getText().toString())) {
            getUserAllPetInfo(false);
        }
    }

    private void intview() {
        mContext = this;
        title = (TextView) findViewById(R.id.title);
        title.setText(FIND);
        findViewById(R.id.back_layout).setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.fragment_pet_listview);
        addTop();
        addFooter();
        mListView.setAdapter(null);
    }

    /**
     * 添加头部
     */
    private void addTop() {
        View topView = LayoutInflater.from(mContext).inflate(R.layout.fragment_pet_addtop, null, false);
        mTopRV = (RecyclerView) topView.findViewById(R.id.fragment_pet_rv);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTopRV.setLayoutManager(linearLayoutManager);
        mListView.addHeaderView(topView);
        mTopAdapter = new PetTopAdapter(mPetsList, new PetTopAdapter.Callback() {
            @Override
            public void callback(int position) {
                selectPosition = position;
                getData(position);
            }
        });
        mTopRV.setAdapter(mTopAdapter);
    }

    /**
     * 当前适配
     */
    private void addFooter() {
        footerView = LayoutInflater.from(mContext).inflate(R.layout.activity_pet_addfooter, null, false);
        name_text = (TextView) footerView.findViewById(R.id.fragment_pet_name);
        phone_text = (TextView) footerView.findViewById(R.id.fragment_pet_phone);
        adress_text = (TextView) footerView.findViewById(R.id.fragment_pet_zhuzhi);
        zhuzhi_title = (TextView) footerView.findViewById(R.id.fragment_pet_zhuzhi_title);
        type_text = (TextView) footerView.findViewById(R.id.fragment_pet_zhuangtai);
        describ_text = (TextView) footerView.findViewById(R.id.activity_issue_miaoshu);
        raward_text = (TextView) footerView.findViewById(R.id.activity_issue_xuanshang);
        tiem_text = (TextView) footerView.findViewById(R.id.fragment_pet_time);
        mFootRV = (RecyclerView) footerView.findViewById(R.id.fragment_pet_rv);
        mSureLayout = (LinearLayout) footerView.findViewById(R.id.surefind_layout);
        mLostLayout = (LinearLayout) footerView.findViewById(R.id.bottom_layout);
        mNormalState = (LinearLayout) footerView.findViewById(R.id.normal_layout);
        lose_text_layout = (LinearLayout) footerView.findViewById(R.id.lose_text_layout);
        footerView.findViewById(R.id.change).setOnClickListener(this);
        footerView.findViewById(R.id.cancel).setOnClickListener(this);
        footerView.findViewById(R.id.find_state).setOnClickListener(this);
        footerView.findViewById(R.id.goon_state).setOnClickListener(this);
        footerView.findViewById(R.id.fabu_state).setOnClickListener(this);
        footerView.findViewById(R.id.change_state).setOnClickListener(this);
        footerView.setVisibility(View.GONE);
        mLostLayout.setVisibility(View.GONE);
        mButton = (Button) footerView.findViewById(R.id.lianxizhuren);
        mButton.setOnClickListener(this);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFootRV.setLayoutManager(linearLayoutManager);
        mListView.addFooterView(footerView);
        mFooterAdapter = new PetFooterAdapter(mPicList, mContext);
        mFootRV.setAdapter(mFooterAdapter);
        mListView.setVisibility(View.GONE);
    }

    /**
     * 选中当前项
     *
     * @param position
     */
    public void getData(int position) {
        if (mPetsList.size() <= 0 || position >= mPetsList.size()) {
            return;
        }
        footerView.setVisibility(View.VISIBLE);
        selectPosition = position;
        mPicList.clear();
        UserPetInfo mUserPetInfo = mPetsList.get(position);
        if (!TextUtils.isEmpty(mUserPetInfo.getPhoto1URL())) {
            mPicList.add(mUserPetInfo.getPhoto1URL());
        }
        if (!TextUtils.isEmpty(mUserPetInfo.getPhoto2URL())) {
            mPicList.add(mUserPetInfo.getPhoto2URL());
        }
        if (!TextUtils.isEmpty(mUserPetInfo.getPhoto3URL())) {
            mPicList.add(mUserPetInfo.getPhoto3URL());
        }
        name_text.setText(mUserPetInfo.getPatName());
        phone_text.setText(mUserPetInfo.getMasterPhone());
        adress_text.setText(TextUtils.isEmpty(mUserPetInfo.getLoseAddress()) ? mUserPetInfo.getHomeAddress() : mUserPetInfo.getLoseAddress());
        type_text.setText(PetState.getState(mUserPetInfo.getState()));
        describ_text.setText(mUserPetInfo.getDescrib());
        raward_text.setText(mUserPetInfo.getReward());
        tiem_text.setText(YKUtil.getStrTime(mUserPetInfo.getLoseDate()));

        mFooterAdapter.notifyDataSetChanged();

        mButton.setVisibility(View.GONE);
        mSureLayout.setVisibility(View.GONE);
        mLostLayout.setVisibility(View.GONE);
        mNormalState.setVisibility(View.GONE);
        lose_text_layout.setVisibility(View.VISIBLE);
        zhuzhi_title.setText("丢失地址:");
        String state = mUserPetInfo.getState();
        if ("lose".equals(state)) {
            if (isFromQRCode) { //二维码进入的界面  显示  联系主人
                mButton.setVisibility(View.VISIBLE);
            } else { //手动点击的界面  显示 修改悬赏-取消悬赏
                mLostLayout.setVisibility(View.VISIBLE);
            }
        } else if ("confirming".equals(state)) {
            if (MY_PET.equals(title.getText().toString())) {//我的宠物 显示 确认找回-继续寻找
                mSureLayout.setVisibility(View.VISIBLE);
            } else if (FIND.equals(title.getText().toString())) {// 发现 显示 放弃联系
                mButton.setVisibility(View.VISIBLE);
                mButton.setText(GIVE_UP_CONTACT);
            }
        } else if ("normal".equals(state)) {
            lose_text_layout.setVisibility(View.GONE);
            zhuzhi_title.setText("住址:");
            if (MY_PET.equals(title.getText().toString())) {//我的宠物 显示 发布悬赏-修改信息
                mNormalState.setVisibility(View.VISIBLE);
            }
        }

        MyManger.saveQRCode(mUserPetInfo.get_$2dCode());
        MyManger.savePicsArray(mPicList);
        UserPetInfo savePetInfo = new UserPetInfo();
        savePetInfo.setPatName(mUserPetInfo.getPatName());
        savePetInfo.setState(mUserPetInfo.getState());
        savePetInfo.setMasterPhone(mUserPetInfo.getMasterPhone());
        savePetInfo.setLoseAddress(mUserPetInfo.getLoseAddress());
        MyManger.savePetInfo(savePetInfo);
    }


    /**
     * 放弃联系
     */
    private void giveUpContac() {
        takePhone(phone_text.getText().toString());
        MyManger.savePetInfo(mPetsList.get(selectPosition));
        mTopRV.setVisibility(View.GONE);
        mButton.setText(GIVE_UP_CONTACT);
        type_text.setText("确认中");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lianxizhuren:
                if (!MyManger.isLogin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                if (GIVE_UP_CONTACT.equals(mButton.getText().toString())) {
                    getAgainInfo();//（放弃联系）
                } else {
                    getSureInfo(); //（联系主人）
                }
                break;
            case R.id.find_state://确认找回
                getFindInfo();
                break;
            case R.id.goon_state://继续寻找
                getAgainInfo();
                break;
            case R.id.change://修改悬赏
                startActivity(new Intent(this, IssueActivity.class));
                break;
            case R.id.cancel://取消悬赏
                getCancelInfo();
                break;
            case R.id.fabu_state://发布悬赏
                startActivity(new Intent(this, IssueActivity.class));
                break;
            case R.id.change_state://修改信息
                startActivity(new Intent(this, ChangePetActivity.class));
                break;
            case R.id.back_layout:
                finish();
                break;
        }
    }

    private void getUserPetInfo() {
        //获取“发现”数据
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", MyManger.getUserInfo().getPhone());
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<ArrayList<UserPetInfo>>(this, RetroFactory.getIstance().getStringService().getFindInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<ArrayList<UserPetInfo>>() {

                    @Override
                    public void onSuccess(ArrayList<UserPetInfo> infos) {
//						Log.e("H", "getFindInfo---->" + infos);
                        if (infos != null && infos.size() > 0) {
                            mPetsList = infos;
//							mTopAdapter.notifyDataSetChanged();
                            getData(0);
                            mTopAdapter.updateData(infos);
                            mFooterAdapter.notifyDataSetChanged();
                            mListView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail() {
//						Log.e("H", "onFail---->");
                    }

                });
    }

    private void getSureInfo() {
        if (!MyManger.isLogin()) {
            startActivity(new Intent(this, RegisterActivity.class));
            return;
        }

        //改变宠物状态（确认中） lose->confirming
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", MyManger.getUserInfo().getPhone());
        map.put("2dCode", MyManger.getQRCode());
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<stringInfo>(this, RetroFactory.getIstance().getStringService().changeLoseToConfirmingState(requestBody))
                .request(new RetroFitUtil.ResponseListener<stringInfo>() {

                    @Override
                    public void onSuccess(stringInfo infos) {
                        if (!TextUtils.isEmpty(infos.getInfo())) {
                            giveUpContac();
                            ToastUtil.showTextToast(getApplicationContext(), infos.getInfo().toString());
                        } else {
                            ToastUtil.showTextToast(getApplicationContext(), infos.getErro());
                        }
                    }

                    @Override
                    public void onFail() {
                        ToastUtil.showTextToast(getApplicationContext(), getResources().getString(R.string.error_net));
                    }

                });
    }


    private void getCancelInfo() {

        //改变宠物状态(取消悬赏) lose->normal
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", MyManger.getUserInfo().getPhone());
        map.put("2dCode", MyManger.getQRCode());
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<stringInfo>(this, RetroFactory.getIstance().getStringService().changeLoseToNormalState(requestBody))
                .request(new RetroFitUtil.ResponseListener<stringInfo>() {

                    @Override
                    public void onSuccess(stringInfo infos) {
                        if (!TextUtils.isEmpty(infos.getInfo())) {
                            if (isFromQRCode) {
                                type_text.setText("正常");
                                mButton.setVisibility(View.GONE);
                            } else {
                                getUserAllPetInfo(false);
                            }
                            ToastUtil.showTextToast(getApplicationContext(), infos.getInfo().toString());
                        } else {
                            ToastUtil.showTextToast(getApplicationContext(), infos.getErro());
                        }
                    }

                    @Override
                    public void onFail() {
                        ToastUtil.showTextToast(getApplicationContext(), getResources().getString(R.string.error_net));
                    }

                });
    }


    private void getAgainInfo() {

        //改变宠物状态（确认失败，继续悬赏） confirming->lose
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", MyManger.getUserInfo().getPhone());
        map.put("2dCode", MyManger.getQRCode());
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<stringInfo>(this, RetroFactory.getIstance().getStringService().changeConfirmingToLoseState(requestBody))
                .request(new RetroFitUtil.ResponseListener<stringInfo>() {

                    @Override
                    public void onSuccess(stringInfo infos) {
                        if (!TextUtils.isEmpty(infos.getInfo())) {
                            ToastUtil.showTextToast(getApplicationContext(), infos.getInfo().toString());
                            finish();
                        } else {
                            ToastUtil.showTextToast(getApplicationContext(), infos.getErro());
                        }
                    }

                    @Override
                    public void onFail() {
                        ToastUtil.showTextToast(getApplicationContext(), getResources().getString(R.string.error_net));
                    }

                });
    }

    private void getFindInfo() {

        //改变宠物状态（宠物找回） confirming->normal
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", MyManger.getUserInfo().getPhone());
        map.put("2dCode", MyManger.getQRCode());
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<stringInfo>(this, RetroFactory.getIstance().getStringService().changeConfirmingToNormalState(requestBody))
                .request(new RetroFitUtil.ResponseListener<stringInfo>() {

                    @Override
                    public void onSuccess(stringInfo infos) {
                        if (!TextUtils.isEmpty(infos.getInfo())) {
                            type_text.setText("正常");
                            mNormalState.setVisibility(View.VISIBLE);
                            ToastUtil.showTextToast(getApplicationContext(), infos.getInfo().toString());
                        } else {
                            ToastUtil.showTextToast(getApplicationContext(), infos.getErro());
                        }
                    }

                    @Override
                    public void onFail() {
                        ToastUtil.showTextToast(getApplicationContext(), getResources().getString(R.string.error_net));
                    }

                });
    }

    private void getUserAllPetInfo(final boolean showLastPosition) {
        // 宠物页 获取用户所有宠物
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", MyManger.getUserInfo().getPhone());
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<ArrayList<UserPetInfo>>(this, RetroFactory.getIstance().getStringService().getUserAllPetInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<ArrayList<UserPetInfo>>() {

                    @Override
                    public void onSuccess(ArrayList<UserPetInfo> infos) {
                        if (infos != null) {
                            mPetsList = infos;
                            mTopAdapter.updateData(infos);
                            if (showLastPosition) {
                                selectPosition = infos.size() - 1;
                            }
                            getData(selectPosition);
                            mListView.setVisibility(View.VISIBLE);
                            mFooterAdapter.notifyDataSetChanged();
                        } else {
                        }
                    }

                    @Override
                    public void onFail() {
                    }

                });
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    private void takePhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
