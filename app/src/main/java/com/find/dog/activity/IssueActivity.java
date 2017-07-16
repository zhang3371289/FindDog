package com.find.dog.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.find.dog.R;
import com.find.dog.image.BitmapUtil;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.BitmapUtilImage;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.PhotoUtil;
import com.find.dog.utils.ToastUtil;
import com.find.dog.utils.YKDeviceInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * 发布悬赏
 */
public class IssueActivity extends BaseActivity implements OnClickListener {
    private Activity mActivity;
    private ArrayList<Bitmap> mapList = new ArrayList<Bitmap>();//本地图片路径集合
    private ArrayList<String> mtempList = new ArrayList<String>();//压缩后图片路径集合
    private String mGetEditText = "";
    private Bitmap tempBitmap;
    private String[] photo_items = new String[]{"选择本地图片", "拍照"};
    private RecyclerView mRecyclerView;
    private IssueAdapter mAdapter;
    private LinearLayout normalLayout;
    private String mName, mAdress;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    //图片上传完成开始 提交回复
                    commitReplay();
                    break;
                case 201:
                    uploadImage();
                    break;
                default:
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_layout);
        mActivity = this;
        initView();
    }


    /**
     * 实例化组件 & 设置监听
     */
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.fragment_pet_rv);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new IssueAdapter(mapList);
        mRecyclerView.setAdapter(mAdapter);
        findViewById(R.id.fabu).setOnClickListener(this);
        findViewById(R.id.change).setOnClickListener(this);
        findViewById(R.id.back_layout).setOnClickListener(this);
        mAdapter.refresh(getIntent().getStringArrayListExtra(UpLoadActivity.PIC_LIST));
        mName = getIntent().getStringExtra(UpLoadActivity.NAME);
        mAdress = getIntent().getStringExtra(UpLoadActivity.ADRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 11://图片选择 返回---zlz

                if (resultCode == RESULT_OK) {
                    ArrayList<String> selected = (ArrayList<String>) data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);

                    mAdapter.refresh(selected);
                }

                break;

			/*拍照返回调用相机*/
            case PhotoUtil.TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        PhotoUtil.startPhotoZoom(PhotoUtil.cameraFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case PhotoUtil.CROP_PHOTO://返回裁剪后图片
                try {
                    if (resultCode == RESULT_OK) {
                        //适用于小米
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(PhotoUtil.cachPath))));
                            Log.d("H", "uritempFile" + new File(PhotoUtil.cachPath));
                            addCropPicture((new File(PhotoUtil.cachPath) + "").replace("file:////", ""));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            default:
                break;
        }


    }

    private void addCropPicture(String path) {
        if (path != null) {
            mAdapter.update(path);

        } else {
            Toast.makeText(mActivity, "裁剪失败，请重试一下吧~", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断图片 是否可 提交
     *
     * @return
     */
    private boolean canCommitFromImage() {
        boolean canCommit = false;
        if (mAdapter.getList().size() <= 0) {
            canCommit = false;
        } else {
            canCommit = true;
        }
        return canCommit;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        /*提交按钮*/
            case R.id.fabu:
                ToastUtil.showTextToast(this, "发布");
                break;
            case R.id.change:
            case R.id.back_layout:
                finish();
            default:
                break;
        }

    }

    /**
     * 相册选择图片或拍照
     */
    public void showChooseImageDialog() {
        new AlertDialog.Builder(this)
                .setTitle("添加图片")
                .setItems(photo_items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0://选择本地图片

                                Intent intentImage = new Intent(mActivity, SelectAlbumActivity.class);
                                //			intentImage.putExtra(SelectPictureActivity.INTENT_MAX_NUM, 3);//选择三张
                                intentImage.putExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE, mAdapter.getList());
                                startActivityForResult(intentImage, 11);

                                break;
                            case 1://拍照

                                if (mAdapter.getList().size() >= 3) {
                                    Toast.makeText(mActivity, "三张图片", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                onPermissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
                                    @Override
                                    public void onClick(boolean bln) {
                                        if (bln) {
                                            PhotoUtil.takePhotoForCamera(IssueActivity.this);
                                        } else {
                                            Toast.makeText(mActivity, "未打开相机权限", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }


    private void zoomPicture() {
        new Thread() {

            @Override
            public void run() {
                //循环挨个压缩
                for (int i = 0; i < mAdapter.getList().size(); i++) {

                    tempBitmap = BitmapUtilImage.getLocationBitmap(mAdapter.getList().get(i));
                    if (tempBitmap != null) {
                        String path = BitmapUtil.saveMyBitmap("temp" + i, tempBitmap, mActivity);
                        mtempList.add(path);

                        if (null != tempBitmap) {

                            tempBitmap.recycle();
                        }
                    } else {
                        mHandler.sendEmptyMessage(203);
                    }

                }

                mHandler.sendEmptyMessage(201);
            }
        }.start();


    }


    /**
     * 请求网络
     * 1.上传图片
     * 2.完成开始提交
     */
    private void uploadImage() {


//		for (String tempPath : mtempList) {
//			YKUploadImageManager.getInstance().uploadImages(tempPath, new UploadImageCallback()
//			{
//				@Override
//				public void callback(YKResult result, String imageUrl)
//				{
//					times ++;
//					if(result.isSucceeded()){
//						size ++;
//						mImageUploadUrl.add(imageUrl);
//
//					}else{
//						Toast.makeText(mActivity, "这一张失败了…", Toast.LENGTH_SHORT).show();
//					}
//					if(times == mAdapter.getList().size()){
//						mHandler.sendEmptyMessage(200);
//					}
//				}
//			});
//		}
    }


    /**
     * 请求网络 提交回复
     */
    private void commitReplay() {

//		YKCommentreplyManager.getInstance().postAeniorReply(YKCurrentUserManager.getInstance().getUser().getToken(), typeId, mGetEditText, mImageUploadUrl,type,new ReplyCallback()
//		{
//			@Override
//			public void callback(YKResult result)
//			{
//
//				mCommit.setEnabled(true);
//				AppUtil.dismissDialogSafe(mCustomButterfly);
//
//				if(result.isSucceeded()){
//					Toast.makeText(mActivity, "发布成功", Toast.LENGTH_SHORT).show();
//					Intent mIntent = new Intent();
//					mIntent.putStringArrayListExtra("ListViewImag", mImageUploadUrl);
//					mIntent.putExtra("textChange", mGetEditText);
//					setResult(66, mIntent);
//					setResult(22,mIntent);
//					finish();
//				}else{
//					Toast.makeText(mActivity, result.getMessage().toString(), Toast.LENGTH_SHORT).show();
//				}
//
//			}
//		});

    }

    public boolean canCommit() {
        if (mGetEditText.length() > 0) {
            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        tempBitmap = null;
        super.onDestroy();
    }


    class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder> {

        private ArrayList<Bitmap> mapList = new ArrayList<>();
        private ArrayList<String> mList = new ArrayList<>();
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

        public IssueAdapter(ArrayList<Bitmap> mImageUrl) {
            this.mapList = mImageUrl;
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
            if (mapList.size() == position) {
                viewHolder.deleteLayout.setVisibility(View.GONE);
                viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    Log.e("H","------"+position);
                        showChooseImageDialog();
                    }
                });
            } else {
                viewHolder.deleteLayout.setVisibility(View.VISIBLE);
//            Glide.with(mContext).load(datas[position])
//                    .placeholder(R.mipmap.ic_launcher)
//                    .error(R.mipmap.ic_launcher)
//                    .into(viewHolder.mImageView);

                viewHolder.mImageView.setImageBitmap((Bitmap) mapList.get(position));
                viewHolder.deleteText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage("确认删除图片?");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mapList.remove(position);
                                mList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(mActivity, "删除图片成功", Toast.LENGTH_SHORT).show();
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
            return mapList.size() + 1;
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
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
            new Thread() {
                public void run() {
                    for (String path : _list) {
                        mapList.add(getLocationBitmap(path, 6));
                    }
                    mHandler.sendEmptyMessage(200);
                }

                ;
            }.start();

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
    }

}
