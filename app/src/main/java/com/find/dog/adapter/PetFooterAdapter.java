package com.find.dog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.find.dog.R;

public class PetFooterAdapter extends RecyclerView.Adapter<PetFooterAdapter.ViewHolder> {

    public String[] datas = null;
    private Context mContext;
    public PetFooterAdapter(String[] datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_pet_footer_item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
//        viewHolder.mImageView.setBackgroundResource();
        Glide.with(mContext).load(datas[position])
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
                .into(viewHolder.mImageView);
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.length;
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public ViewHolder(View view){
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.fragmet_pet_top_img);
        }
    }
}