package com.find.dog.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.find.dog.R;
import com.find.dog.utils.BitmapUtilImage;
import com.find.dog.utils.QINiuUtil;
import com.find.dog.utils.YKDeviceInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UpLoadAdapter extends RecyclerView.Adapter<UpLoadAdapter.ViewHolder> {

    private ArrayList<Bitmap> mapList = new ArrayList<>();
    private ArrayList<String> mList = new ArrayList<>();
    private Context mContext;
    private Callback callback;
    private ViewHolder mHolder;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 200:
                    notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    public UpLoadAdapter(Context mContext,final Callback callback) {
        this.mContext = mContext;
        this.callback = callback;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_upload_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        mHolder = viewHolder;
        if (mapList.size() == position) {
            viewHolder.deleteLayout.setVisibility(View.GONE);
            viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.e("H","------"+position);
                    if (callback != null) {
                        callback.callback();
                    }
                }
            });
        } else {
            viewHolder.deleteLayout.setVisibility(View.VISIBLE);

            viewHolder.mImageView.setImageBitmap((Bitmap) mapList.get(position));
            viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            viewHolder.deleteText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("确认删除图片?");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mapList.remove(position);
                            mList.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(mContext, "删除图片成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });

        }

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        int length = mapList.size() + 1 > 3 ? 3 :  mapList.size() + 1;
        return length;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        RelativeLayout deleteLayout, mLayout;
        TextView deleteText;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.activity_upload_item_img);
            deleteLayout = (RelativeLayout) view.findViewById(R.id.activity_upload_item_delete_layout);
            mLayout = (RelativeLayout) view.findViewById(R.id.activity_upload_item_layout);
            deleteText = (TextView) view.findViewById(R.id.activity_upload_item_delete);
        }
    }

    public void refresh(final ArrayList<String> _list) {
        mList = _list;
        mapList.clear();
        for (final String path : _list) {

           if(path.contains(QINiuUtil.photo_value)){//网络图片转bitmap
               Glide.with(mContext).load(path+ QINiuUtil.photo_suffix).asBitmap().into(new SimpleTarget<Bitmap>() {
                   @Override
                   public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                       mapList.add(resource);
                       notifyDataSetChanged();
//                       mHandler.sendEmptyMessage(200);
                   }
               });
           }else {//本地图片转bitmap
               new Thread() {
                   public void run() {
                       mapList.add(getLocationBitmap(path, 6));
                       mHandler.sendEmptyMessage(200);
                   } ;
               }.start();
           }
        }

    }


    public void update(final String path) {
        mList.add(path);
        new Thread() {
            public void run() {
                mapList.add(getLocationBitmap(path, 2));
                mHandler.sendEmptyMessage(200);
            }

            ;
        }.start();
    }

    public ArrayList<String> getList() {
        return mList;
    }

    public Bitmap getLocationBitmap(String url, int size) {
        size = BitmapUtilImage.getZoomSize(url);
        Bitmap bitmap = null;
        //GT-N7102三星
        /*设备型号*/
        String phonemodel = YKDeviceInfo.getDeviceModel();
        try {
            FileInputStream fis = new FileInputStream(url);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = size;   // width，hight设为原来的十分一
            int orientation = BitmapUtilImage.readPictureDegree(url);//获取旋转角度
            bitmap = BitmapFactory.decodeStream(fis, null, options);
            if (Math.abs(orientation) > 0) {
                bitmap = BitmapUtilImage.rotaingImageView(orientation, bitmap);//旋转图片
            }
            return bitmap;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public interface Callback {
        public void callback();
    }
}