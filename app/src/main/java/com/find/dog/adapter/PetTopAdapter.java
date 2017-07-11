package com.find.dog.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.find.dog.R;
import com.find.dog.fragment.PetFragment;

public class PetTopAdapter extends RecyclerView.Adapter<PetTopAdapter.ViewHolder> {
    PetFragment mContext;

//    public String[] datas = null;
    public PetTopAdapter(PetFragment mContext) {
//        this.datas = datas;
        this.mContext = mContext;
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
                mContext.getData(position);
            }
        });
    }
    //获取数据的数量
    @Override
    public int getItemCount() {
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
}