package com.find.dog.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.find.dog.R;
import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.adapter.PetFooterAdapter;
import com.find.dog.adapter.PetTopAdapter;
import com.find.dog.data.UserPetInfo;
import com.find.dog.main.BaseActivity;
import com.find.dog.main.MyApplication;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 *  Created by zhangzhongwei on 2017/7/11.
 * 我的宠物
 */
public class MyPetActivity extends BaseActivity implements View.OnClickListener{
	private ListView mListView;
	private RecyclerView mTopRV;
	private Context mContext;
	private PetFooterAdapter mFooterAdapter;
	private ArrayList<String> mPicList = new ArrayList<String>();//图片路径集合
	private String mName,mAdress;
	private TextView name_text,type_text,phone_text,adress_text;
	private ArrayList<UserPetInfo> mPetsList = new ArrayList<>();
	private PetTopAdapter mTopAdapter;
	private int selectPosition = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypet_layout);
		intview();
		getUserAllPetInfo();
	}


	private void intview() {
		findViewById(R.id.back_layout).setOnClickListener(this);
		mContext = this;
		mListView = (ListView) findViewById(R.id.fragment_pet_listview);
		addTop();
		addFooter();
		mListView.setAdapter(null);
	}

	private void getUserAllPetInfo(){
		//获取用户所有宠物
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
							getData(infos.size()-1);
						} else {
						}
					}

					@Override
					public void onFail() {
					}

				});
	}

	/**
	 * 选中当前项
	 * @param position
	 */
	public void getData(int position){
		if(mPetsList.size()<=0){
			return;
		}
		selectPosition = position;
		mPicList.clear();
		UserPetInfo mUserPetInfo = mPetsList.get(position);
		if(!TextUtils.isEmpty(mUserPetInfo.getPhoto1URL())){
			mPicList.add(mUserPetInfo.getPhoto1URL());
		}
		if(!TextUtils.isEmpty(mUserPetInfo.getPhoto2URL())){
			mPicList.add(mUserPetInfo.getPhoto2URL());
		}
		if(!TextUtils.isEmpty(mUserPetInfo.getPhoto3URL())){
			mPicList.add(mUserPetInfo.getPhoto3URL());
		}

		name_text.setText(mUserPetInfo.getPatName());
		phone_text.setText(mUserPetInfo.getMasterPhone());
		adress_text.setText(mUserPetInfo.getLoseAddress());
		type_text.setText(mUserPetInfo.getState());
		mFooterAdapter.notifyDataSetChanged();
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
				getData(position);
			}
		});
		mTopRV.setAdapter(mTopAdapter);
	}

	/**
	 * 当前适配
	 */
	private void addFooter() {
		View footerView = LayoutInflater.from(mContext).inflate(R.layout.fragment_pet_addfooter, null, false);
		name_text = (TextView) footerView.findViewById(R.id.fragment_pet_name);
		phone_text = (TextView) footerView.findViewById(R.id.fragment_pet_phone);
		adress_text = (TextView) footerView.findViewById(R.id.fragment_pet_zhuzhi);
		type_text = (TextView) footerView.findViewById(R.id.fragment_pet_zhuangtai);
		mTopRV = (RecyclerView) footerView.findViewById(R.id.fragment_pet_rv);
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mTopRV.setLayoutManager(linearLayoutManager);
		mListView.addFooterView(footerView);
		mFooterAdapter = new PetFooterAdapter(mPicList,mContext);
		mTopRV.setAdapter(mFooterAdapter);
		footerView.findViewById(R.id.fabu).setOnClickListener(this);
		footerView.findViewById(R.id.change).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.fabu:
				MyManger.saveQRCode(mPetsList.get(selectPosition).get_$2dCode());
				MyManger.savePicsArray(mPicList);
				Intent intent = new Intent(this,IssueActivity.class);
				startActivity(intent);
				break;
			case R.id.change:
				MyManger.saveQRCode(mPetsList.get(selectPosition).get_$2dCode());
				MyManger.savePicsArray(mPicList);
				Intent intent1 = new Intent(this,ChangePetActivity.class);
				startActivity(intent1);
				break;
			case R.id.back_layout:
				finish();
				break;
		}

	}
}
