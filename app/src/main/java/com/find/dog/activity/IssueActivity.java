package com.find.dog.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.find.dog.R;
import com.find.dog.adapter.UpLoadAdapter;
import com.find.dog.data.UserPetInfo;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.ChoseCityUtil;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.PhotoUtil;
import com.find.dog.utils.YKUtil;
import com.lljjcoder.citypickerview.widget.CityPicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * 发布悬赏
 */
public class IssueActivity extends BaseActivity implements OnClickListener {
    private Activity mActivity;
    private String[] photo_items = new String[]{"选择本地图片", "拍照"};
    private RecyclerView mRecyclerView;
    private UpLoadAdapter mAdapter;
    private TextView name_text,phone_text,losttime_text,pet_state;
    private EditText issue_edit,description_edit,adress_edit;

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
        mAdapter = new UpLoadAdapter(this, new UpLoadAdapter.Callback() {
            @Override
            public void callback() {
                showChooseImageDialog();
            }
        });
        mAdapter.refresh(MyManger.loadPicsArray());
        mRecyclerView.setAdapter(mAdapter);
        findViewById(R.id.fabu).setOnClickListener(this);
        findViewById(R.id.change).setOnClickListener(this);
        findViewById(R.id.back_layout).setOnClickListener(this);

        description_edit = (EditText) findViewById(R.id.activity_issue_description);
        adress_edit = (EditText) findViewById(R.id.activity_issue_adress);
        issue_edit = (EditText) findViewById(R.id.activity_issue_issue);
        name_text = (TextView) findViewById(R.id.fragment_pet_name);
        phone_text = (TextView) findViewById(R.id.fragment_pet_phone);
        losttime_text = (TextView) findViewById(R.id.fragment_pet_time);
        pet_state = (TextView) findViewById(R.id.fragment_pet_zhuangtai);

        name_text.setText(MyManger.getPetInfo().getPatName());
        phone_text.setText(MyManger.getPetInfo().getMasterPhone());
        adress_edit.setText(MyManger.getPetInfo().getLoseAddress());
        adress_edit.setOnClickListener(this);
        issue_edit.setText(MyManger.getMoney());
        description_edit.setText(MyManger.getDescrib());

        losttime_text.setText(YKUtil.getStrTime(YKUtil.getUnixStamp()+""));
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
                break;

//            case CityListSelectActivity.CITY_SELECT_RESULT_FRAG:
//                if (resultCode == RESULT_OK) {
//                    if (data == null) {
//                        return;
//                    }
//                    Bundle bundle = data.getExtras();
//
//                    CityInfoBean cityInfoBean = (CityInfoBean) bundle.getParcelable("cityinfo");
//
//                    if (null == cityInfoBean)
//                        return;
//
//                    //城市名称
//                    String cityName = cityInfoBean.getName();
//                    //纬度
//                    String latitude = cityInfoBean.getLongitude();
//                    //经度
//                    String longitude = cityInfoBean.getLatitude();
//
//                    //获取到城市名称，经纬度值后可自行使用...
//
//                    adress_text.setText(cityName);
//                }
//                break;

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



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        /*提交按钮*/
            case R.id.fabu:
                MyManger.saveMoney(issue_edit.getText().toString());
                MyManger.saveDescrib(description_edit.getText().toString());
                MyManger.savePicsArray(mAdapter.getList());
                UserPetInfo pet = new UserPetInfo();
                pet.setLoseAddress(adress_edit.getText().toString());
                pet.setPatName(name_text.getText().toString());
                pet.setMasterPhone(phone_text.getText().toString());
                pet.setLoseDate(losttime_text.getText().toString());
                MyManger.savePetInfo(pet);
                Intent intent = new Intent(this,PayActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.change:
            case R.id.back_layout:
                finish();
                break;
            case R.id.activity_issue_adress:
                ChoseCityUtil.showDialog(this,adress_edit);
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

}
