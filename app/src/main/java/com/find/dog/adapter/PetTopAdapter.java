package com.find.dog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.find.dog.R;
import com.find.dog.activity.MyPetActivity;
import com.find.dog.data.UserPetInfo;

import java.util.ArrayList;

public class PetTopAdapter extends RecyclerView.Adapter<PetTopAdapter.ViewHolder> {
    ArrayList<UserPetInfo> mPetsList;
    Callback callback;
    public PetTopAdapter(ArrayList<UserPetInfo> mPetsList,final Callback callback) {
        this.mPetsList = mPetsList;
        this.callback = callback;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_pet_top_item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback(position);
            }
        });
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        int length = mPetsList == null ? 0 : mPetsList.size();
        return 3;
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public ViewHolder(View view){
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.fragmet_pet_top_img);
        }
    }

    public interface Callback {
        public void callback(int position);
    }
}