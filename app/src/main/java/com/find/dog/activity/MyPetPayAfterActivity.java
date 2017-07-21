package com.find.dog.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.find.dog.R;
import com.find.dog.adapter.PetFooterAdapter;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.MyManger;

import java.util.ArrayList;

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
	private TextView name_text,type_text,phone_text,adress_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypet_layout);
		intview();
	}


	private void intview() {
		findViewById(R.id.back_layout).setOnClickListener(this);
		mPicList = MyManger.loadPicsArray();
		mName = getIntent().getStringExtra(UpLoadActivity.NAME);
		mAdress = getIntent().getStringExtra(UpLoadActivity.ADRESS);
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
		PetTopAdapter mTopAdapter = new PetTopAdapter();
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.change:
				Intent intent1 = new Intent(this,IssueActivity.class);
				intent1.putExtra(UpLoadActivity.NAME,mName);
				intent1.putExtra(UpLoadActivity.ADRESS,mAdress);
				startActivity(intent1);
				break;
			case R.id.cancel:
			case R.id.back_layout:
				finish();
				break;
		}

	}


	class PetTopAdapter extends RecyclerView.Adapter<PetTopAdapter.ViewHolder> {
		//    public String[] datas = null;
		//创建新View，被LayoutManager所调用
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
			View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_pet_top_item, viewGroup, false);
			ViewHolder vh = new ViewHolder(view);
			return vh;
		}

		//将数据与界面进行绑定的操作
		@Override
		public void onBindViewHolder(ViewHolder viewHolder, final int position) {
			viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getData(position);
				}
			});
		}

		//获取数据的数量
		@Override
		public int getItemCount() {
			return 3;
		}

		//自定义的ViewHolder，持有每个Item的的所有界面元素
		public class ViewHolder extends RecyclerView.ViewHolder {
			public ImageView mImageView;

			public ViewHolder(View view) {
				super(view);
				mImageView = (ImageView) view.findViewById(R.id.fragmet_pet_top_img);
			}
		}
	}
}
