package com.find.dog.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.find.dog.adapter.PetFooterAdapter;
import com.find.dog.adapter.PetTopAdapter;
import com.find.dog.R;

import java.util.ArrayList;

/**
 * Created by zhangzhongwei on 2017/6/27.
 * 宠物Fragment
 */

public class PetFragment extends Fragment {
    private ListView mListView;
    private RecyclerView mTopRV;
    private Context mContext;
    private TextView name;
    private PetFooterAdapter mFooterAdapter;
    private ArrayList<String> mPicList = new ArrayList<String>();//图片路径集合
    private static int selectPosition = 0;
    private String[] data1 = {"http://pic1.win4000.com/wallpaper/2/57e0fc465d511.jpg"
            ,"http://pic1.win4000.com/wallpaper/2/57e0fc465d511.jpg"
            ,"http://pic1.win4000.com/wallpaper/2/57e0fc465d511.jpg"
            ,"http://pic1.win4000.com/wallpaper/2/57e0fc465d511.jpg"
            ,"http://pic1.win4000.com/wallpaper/2/57e0fc465d511.jpg"
            ,"http://pic1.win4000.com/wallpaper/2/57e0fc465d511.jpg"};
    private String[] data2 = {"http://www.bz55.com/uploads/allimg/130618/1-13061PU434.jpg"
            ,"http://www.bz55.com/uploads/allimg/130618/1-13061PU434.jpg"
            ,"http://www.bz55.com/uploads/allimg/130618/1-13061PU434.jpg"};
    private String[] data3 = {"http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg"
            ,"http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg"
            ,"http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg"
            ,"http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg"
            ,"http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg"
            ,"http://imgsrc.baidu.com/imgad/pic/item/1e30e924b899a9017c518d1517950a7b0208f5a9.jpg"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pet, container, false);
        intview(rootView);
        return rootView;
    }

    private void intview(View rootView) {
        mContext = getActivity();
        mListView = (ListView) rootView.findViewById(R.id.fragment_pet_listview);
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
        name.setText("选中"+position);
        switch (position){
            case 0:
                mPicList.clear();
                for(int i=0;i<data1.length;i++){
                    mPicList.add(data1[i]);
                }
                break;
            case 1:
                mPicList.clear();
                for(int i=0;i<data2.length;i++){
                    mPicList.add(data2[i]);
                }
                break;
            case 2:
                mPicList.clear();
                for(int i=0;i<data3.length;i++){
                    mPicList.add(data3[i]);
                }
                break;
        }
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
        PetTopAdapter mTopAdapter = new PetTopAdapter(PetFragment.this);
        mTopRV.setAdapter(mTopAdapter);
    }

    /**
     * 当前适配
     */
    private void addFooter() {
        View footerView = LayoutInflater.from(mContext).inflate(R.layout.fragment_pet_addfooter, null, false);
        name = (TextView) footerView.findViewById(R.id.fragment_pet_name);
        mTopRV = (RecyclerView) footerView.findViewById(R.id.fragment_pet_rv);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTopRV.setLayoutManager(linearLayoutManager);
        mListView.addFooterView(footerView);
        mFooterAdapter = new PetFooterAdapter(mPicList,mContext);
        mTopRV.setAdapter(mFooterAdapter);
    }
}
