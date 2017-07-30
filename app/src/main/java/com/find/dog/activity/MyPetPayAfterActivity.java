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
import com.find.dog.data.stringInfo;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 *  Created by zhangzhongwei on 2017/7/11.
 * 我的宠物
 */
public class MyPetPayAfterActivity extends BaseActivity implements View.OnClickListener{
	private ListView mListView;
	private RecyclerView mTopRV;
	private Context mContext;
	private PetFooterAdapter mFooterAdapter;
	private static int selectPosition = 0;
	private ArrayList<String> mPicList = new ArrayList<String>();//图片路径集合
	private String mName,mAdress;
	private ArrayList<UserPetInfo> mPetsList = new ArrayList<>();
	private TextView name_text,type_text,phone_text,adress_text,losttime_text,money_text,describ_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypet_layout);
		intview();
	}


	private void intview() {
		findViewById(R.id.back_layout).setOnClickListener(this);
//		mPicList = MyManger.loadPicsArray();
		mName = MyManger.getUserInfo().getName();
		mAdress =MyManger.getUserInfo().getAdress();
		mContext = this;
		mListView = (ListView) findViewById(R.id.fragment_pet_listview);
		addTop();
		addFooter();
		mListView.setAdapter(null);
		getData(0);
	}

	/**
	 * 选中当前项
	 * @param position
	 */
	public void getData(int position){
		selectPosition = position;
		mFooterAdapter = new PetFooterAdapter(mPicList,mContext);
		mTopRV.setAdapter(mFooterAdapter);
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
		PetTopAdapter mTopAdapter = new PetTopAdapter(mPetsList, new PetTopAdapter.Callback() {
			@Override
			public void callback(int position) {
				ToastUtil.showTextToast(mContext,"选中"+position);
			}
		});
		mTopRV.setAdapter(mTopAdapter);
	}

	/**
	 * 当前适配
	 */
	private void addFooter() {
		View footerView = LayoutInflater.from(mContext).inflate(R.layout.activity_pet_addfooter, null, false);
		name_text = (TextView) footerView.findViewById(R.id.fragment_pet_name);
		phone_text = (TextView) footerView.findViewById(R.id.fragment_pet_phone);
		adress_text = (TextView) footerView.findViewById(R.id.fragment_pet_zhuzhi);
		type_text = (TextView) footerView.findViewById(R.id.fragment_pet_zhuangtai);
		losttime_text = (TextView) footerView.findViewById(R.id.fragment_pet_time);
		money_text = (TextView) footerView.findViewById(R.id.activity_issue_xuanshang);
		describ_text = (TextView) footerView.findViewById(R.id.activity_issue_miaoshu);
		mTopRV = (RecyclerView) footerView.findViewById(R.id.fragment_pet_rv);
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mTopRV.setLayoutManager(linearLayoutManager);
		mListView.addFooterView(footerView);
		mFooterAdapter = new PetFooterAdapter(mPicList,mContext);
		mTopRV.setAdapter(mFooterAdapter);
		name_text.setText(mName);
		phone_text.setText(MyManger.getUserInfo().getPhone());
		adress_text.setText(mAdress);
		footerView.findViewById(R.id.change).setOnClickListener(this);
		footerView.findViewById(R.id.cancel).setOnClickListener(this);
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new java.util.Date());
		losttime_text.setText(date);
		money_text.setText(MyManger.getMoney());
		describ_text.setText(MyManger.getDescrib());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.change:
				Intent intent1 = new Intent(this,IssueActivity.class);
				startActivity(intent1);
				break;
			case R.id.cancel:
				cancleIssue();
				break;
			case R.id.back_layout:
				finish();
				break;
		}

	}

	private void cancleIssue(){

		if("正常".equals(type_text.getText().toString())){
			finish();
			return;
		}
		//改变宠物状态(取消悬赏) lose->normal
		Map<String, String> map = new HashMap<>();
		map.put("userPhone", MyManger.getUserInfo().getPhone());
		map.put("2dCode", MyManger.getQRCode());
		RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
		new RetroFitUtil<stringInfo>(this, RetroFactory.getIstance().getStringService().changeLoseToNormalState(requestBody))
				.request(new RetroFitUtil.ResponseListener<stringInfo>() {

					@Override
					public void onSuccess(stringInfo infos) {
						Log.e("H", "getRegistPetInfo---->" + infos);
						if (!TextUtils.isEmpty(infos.getInfo())) {
							ToastUtil.showTextToast(getApplicationContext(), infos.getInfo());
							type_text.setText("正常");
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

}
