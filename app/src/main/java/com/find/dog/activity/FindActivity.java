package com.find.dog.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.find.dog.R;
import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.adapter.PetFooterAdapter;
import com.find.dog.adapter.PetTopAdapter;
import com.find.dog.data.UserPetInfo;
import com.find.dog.main.BaseActivity;
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
public class FindActivity extends BaseActivity implements View.OnClickListener{
	private ListView mListView;
	private RecyclerView mTopRV;
	private Context mContext;
	private PetFooterAdapter mFooterAdapter;
	private static int selectPosition = 0;
	private ArrayList<String> mPicList = new ArrayList<String>();//图片路径集合
	private TextView name_text,type_text,phone_text,adress_text,title;
	private Button mButton,mSure,mFind;
	private LinearLayout mSureLayout;
	private PetTopAdapter mTopAdapter;
	private ArrayList<UserPetInfo> mPetsList = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypet_layout);
		intview();
		getUserPetInfo();
	}


	private void intview() {
		mContext = this;
		title = (TextView) findViewById(R.id.title);
		title.setText("发现");
		findViewById(R.id.back_layout).setOnClickListener(this);
		mPicList = MyManger.loadPicsArray();
		mListView = (ListView) findViewById(R.id.fragment_pet_listview);
		addTop();
		addFooter();
		mListView.setAdapter(null);
		getData(0);
	}

	private void getUserPetInfo(){
		//获取用户所有宠物
		Map<String, String> map = new HashMap<>();
//		map.put("userphone", "111");
		RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
		new RetroFitUtil<ArrayList<UserPetInfo>>(this, RetroFactory.getIstance().getStringService().getUserPetInfo(requestBody))
				.request(new RetroFitUtil.ResponseListener<ArrayList<UserPetInfo>>() {

					@Override
					public void onSuccess(ArrayList<UserPetInfo> infos) {
						Log.e("H", "getUserPetInfo---->" + infos);
						ToastUtil.showTextToast(mContext,infos.toString());
						if (infos != null) {
							mPetsList = infos;
							mTopAdapter.notifyDataSetChanged();
							mFooterAdapter.notifyDataSetChanged();
						} else {
						}
					}

					@Override
					public void onFail() {
						Log.e("H", "onFail---->");
					}

				});
	}


	/**
	 * 选中当前项
	 * @param position
	 */
	public void getData(int position){
		selectPosition = position;
//		switch (position){
//			case 0:
//				datas = data1;
//				break;
//			case 1:
//				datas = data2;
//				break;
//			case 2:
//				datas = data3;
//				break;
//		}
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
		mTopAdapter = new PetTopAdapter(mPetsList, new PetTopAdapter.Callback() {
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
		mTopRV = (RecyclerView) footerView.findViewById(R.id.fragment_pet_rv);
		mSure = (Button) footerView.findViewById(R.id.change);
		mFind = (Button) footerView.findViewById(R.id.cancel);
		mSure.setOnClickListener(this);
		mFind.setOnClickListener(this);
		mSureLayout = (LinearLayout) footerView.findViewById(R.id.bottom_layout);
		mSureLayout.setVisibility(View.GONE);
		mButton = (Button) footerView.findViewById(R.id.lianxizhuren);
		mButton.setOnClickListener(this);
		mButton.setVisibility(View.VISIBLE);
		//设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mTopRV.setLayoutManager(linearLayoutManager);
		mListView.addFooterView(footerView);
		mFooterAdapter = new PetFooterAdapter(mPicList,mContext);
		mTopRV.setAdapter(mFooterAdapter);

		updateData();
	}

	private void updateData(){
		name_text.setText(MyManger.getUserInfo().getName());
		phone_text.setText(MyManger.getUserInfo().getPhone());
		adress_text.setText(MyManger.getUserInfo().getAdress());
	}

	/**
	 * 放弃联系
	 */
	private void giveUpContac(){
		updateData();
		mButton.setText("放弃联系");
		type_text.setText("确认中");
	}

	private void sureFind(){
		mButton.setVisibility(View.GONE);
		mSureLayout.setVisibility(View.VISIBLE);
		mSure.setText("确认找回");
		mFind.setText("继续寻找");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.lianxizhuren:
				if("放弃联系".equals(mButton.getText().toString())){
					sureFind();
				}else{
					Intent intent1 = new Intent(this,RegisterActivity.class);
					startActivityForResult(intent1,RegisterActivity.REGIST_RESULT);
				}
				break;
			case R.id.change:
				Intent intent1 = new Intent(this,MyPetActivity.class);
				startActivity(intent1);
				break;
			case R.id.back_layout:
				finish();
				break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==RegisterActivity.REGIST_RESULT && resultCode==RegisterActivity.REGIST_RESULT){
			giveUpContac();
		}
	}
}
