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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.find.dog.data.stringInfo;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.PhotoUtil;
import com.find.dog.utils.QINiuUtil;
import com.find.dog.utils.ToastUtil;
import com.find.dog.utils.YKUtil;

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
    private String[] photo_items = new String[]{"选择本地图片", "拍照"};
    private RecyclerView mRecyclerView;
    private UpLoadAdapter mAdapter;
    private LinearLayout normalLayout;
    private EditText mNameEdit, mAdressEdit, mPhoneEdit;


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
//        mAdapter.refresh(MyManger.loadPicsArray());
        mCommit = (Button) findViewById(R.id.activity_upload_up);
        mCommit.setOnClickListener(this);
        findViewById(R.id.back_layout).setOnClickListener(this);
        findViewById(R.id.activity_upload_yzm_text).setOnClickListener(this);
        normalLayout = (LinearLayout) findViewById(R.id.activity_upload_normal_layout);
        mNameEdit.setText(MyManger.getUserInfo().getName());
        mAdressEdit.setText(MyManger.getUserInfo().getAdress());
        mPhoneEdit.setText(MyManger.getUserInfo().getPhone());
        if (!MyManger.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LoginActivity.LOGIN_RESULT);
        }else{
            normalLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 七牛 上传图片
     */
    private void uploadPic(){
        if(!YKUtil.isNetworkAvailable()){
            ToastUtil.showTextToast(this,getResources().getString(R.string.intent_no));
            return;
        }

        QINiuUtil.getInstance().uploadPic(this,mAdapter.getList(), new QINiuUtil.Callback() {
            @Override
            public void callback(boolean isOk,Map<String, String> pic_map) {
                if(isOk){
                    getRegistPetInfo(pic_map);
                }else{
                    QINiuUtil.dismissDialog();
                }
            }
        });
    }

    /**
     * 上传 信息
     * @param pic_map
     */
    private void getRegistPetInfo(final Map<String, String> pic_map) {

        //宠物信息录入
        Map<String, String> map = new HashMap<>();
        map.put("userPhone", mPhoneEdit.getText().toString());
        map.put("patName", mNameEdit.getText().toString());
        map.put("homeAddress", mAdressEdit.getText().toString());
        map.put("2dCode", MyManger.getQRCode());
        map.putAll(pic_map);
        RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
        new RetroFitUtil<stringInfo>(this, RetroFactory.getIstance().getStringService().getRegistPetInfo(requestBody))
                .request(new RetroFitUtil.ResponseListener<stringInfo>() {

                    @Override
                    public void onSuccess(stringInfo infos) {
                        QINiuUtil.dismissDialog();
                        if (!TextUtils.isEmpty(infos.getInfo())) {
                            UserInfo info = new UserInfo();
                            info.setName(mNameEdit.getText().toString());
                            info.setAdress(mAdressEdit.getText().toString());
                            info.setPhone(mPhoneEdit.getText().toString());
                            MyManger.saveUserInfo(info);
                            Intent intent = new Intent(mActivity, FindActivity.class);
                            intent.putExtra("isFormUpLoad",true);
                            startActivity(intent);
                            // 将Map value 转化为List
                            ArrayList<String> mapValuesList = new ArrayList<String>(pic_map.values());
                            MyManger.savePicsArray(mapValuesList);
//                            finish();
                        } else {
                            ToastUtil.showTextToast(getApplicationContext(), infos.getErro());
                        }
                    }

                    @Override
                    public void onFail() {
                        QINiuUtil.dismissDialog();
                        ToastUtil.showTextToast(getApplicationContext(), getResources().getString(R.string.error_net));
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
                    updateCommitState();
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
                break;

            case LoginActivity.LOGIN_RESULT:
                mPhoneEdit.setText(MyManger.getUserInfo().getPhone());
                break;

            default:
                break;
        }

    }

    private void addCropPicture(String path) {
        if (path != null) {
            mAdapter.update(path);
            /*提交按钮可点击*/
        } else {
            Toast.makeText(mActivity, "裁剪失败，请重试一下吧~", Toast.LENGTH_SHORT).show();
        }
        updateCommitState();
    }

    private void updateCommitState(){
        if (mAdapter.getList().size() > 0) {
            mCommit.setTextColor(getResources().getColor(R.color.red));
            mCommit.setEnabled(true);
        } else {
            mCommit.setTextColor(getResources().getColor(R.color.c_8899a6));
            mCommit.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        /*提交按钮*/
            case R.id.activity_upload_up:
                if(MyManger.isLogin()){
                    uploadPic();
                }else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent,  LoginActivity.LOGIN_RESULT);
                }
                break;
            case R.id.back_layout:
                finish();
                break;
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

}
