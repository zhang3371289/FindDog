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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.find.dog.R;
import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.adapter.UpLoadAdapter;
import com.find.dog.data.UserInfo;
import com.find.dog.data.rewardingInfo;
import com.find.dog.data.stringInfo;
import com.find.dog.image.BitmapUtil;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.BitmapUtilImage;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.PhotoUtil;
import com.find.dog.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 上传资料
 */
public class UpLoadActivity extends BaseActivity implements OnClickListener {
    public Button mCommit;
    private Activity mActivity;
    private ArrayList<String> mtempList = new ArrayList<String>();//压缩后图片路径集合
    private Bitmap tempBitmap;
    private String[] photo_items = new String[]{"选择本地图片", "拍照"};
    private RecyclerView mRecyclerView;
    private UpLoadAdapter mAdapter;
    private LinearLayout normalLayout;
    private EditText mNameEdit, mAdressEdit, mPhoneEdit;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 201:
                    uploadImage();
                    break;
                case 203:
                    mCommit.setEnabled(true);
                    break;
                default:
                    break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_layout);
        mActivity = this;
        initView();
    }


    /**
     * 实例化组件 & 设置监听
     */
    private void initView() {
        mNameEdit = (EditText) findViewById(R.id.activity_upload_name_edit);
        mAdressEdit = (EditText) findViewById(R.id.activity_upload_adress_edit);
        mPhoneEdit = (EditText) findViewById(R.id.activity_upload_phone_edit);
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_upload_rv);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new UpLoadAdapter(this, new UpLoadAdapter.Callback() {
            @Override
            public void callback() {
                showChooseImageDialog();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.refresh(MyManger.loadPicsArray());
        mCommit = (Button) findViewById(R.id.activity_upload_up);
        mCommit.setOnClickListener(this);
        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.activity_upload_yzm_text).setOnClickListener(this);
        normalLayout = (LinearLayout) findViewById(R.id.activity_upload_normal_layout);
        if (MyManger.isLogin()) {
            normalLayout.setVisibility(View.GONE);
        } else {
            normalLayout.setVisibility(View.VISIBLE);
        }
    }

    private void getRegistPetInfo() {
        //宠物信息录入
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", "北京市");
        map.put("patName", "北京市");
        map.put("photo1URL", "北京市");
        map.put("photo1URL", "北京市");
        map.put("photo1URL", "北京市");
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<stringInfo>(this, RetroFactory.getIstance().getStringService().getRegistPetInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<stringInfo>() {

                    @Override
                    public void onSuccess(stringInfo infos) {
                        Log.e("H", "getRegistPetInfo---->" + infos);
                        ToastUtil.showTextToast(getApplicationContext(),infos.toString());
                        if (infos != null) {

                            UserInfo info = new UserInfo();
                            info.setName(mNameEdit.getText().toString());
                            info.setAdress(mAdressEdit.getText().toString());
                            info.setPhone(mPhoneEdit.getText().toString());
                            MyManger.saveUserInfo(info);
                            MyManger.savePicsArray(mAdapter.getList());
                            Intent intent = new Intent(mActivity, MyPetActivity.class);
                            startActivity(intent);

                        } else {
                        }
                    }

                    @Override
                    public void onFail() {
                    }

                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 11://图片选择 返回---zlz

                if (resultCode == RESULT_OK) {
                    ArrayList<String> selectedList = (ArrayList<String>) data.getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);

                    mAdapter.refresh(selectedList);
                    //ImageList.size > 0 ? 可提交（红色） ：不可提交（黑色）
                    if (canCommitFromImage()) {
                        mCommit.setTextColor(getResources().getColor(R.color.red));
                        mCommit.setEnabled(true);
                    } else {
                        mCommit.setTextColor(getResources().getColor(R.color.location_city_gps));
                        mCommit.setEnabled(false);
                    }
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
            /*提交按钮可点击*/
            mCommit.setTextColor(getResources().getColor(R.color.red));
            mCommit.setEnabled(true);

        } else {
            Toast.makeText(mActivity, "裁剪失败，请重试一下吧~", Toast.LENGTH_SHORT).show();
            mCommit.setTextColor(getResources().getColor(R.color.location_city_gps));
            mCommit.setEnabled(false);
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
            case R.id.activity_upload_up:
                getRegistPetInfo();
                break;
            case R.id.back_layout:
                finish();
            case R.id.activity_upload_yzm_text:
                ToastUtil.showTextToast(this, "获取验证码");
                break;
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
                                            PhotoUtil.takePhotoForCamera(UpLoadActivity.this);
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
//		}
    }


    @Override
    protected void onDestroy() {
        tempBitmap = null;
        super.onDestroy();
    }

}
