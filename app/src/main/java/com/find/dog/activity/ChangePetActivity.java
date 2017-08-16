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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.zxing.activity.CaptureActivity;
import com.lljjcoder.citypickerview.widget.CityPicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * 修改信息
 */
public class ChangePetActivity extends BaseActivity implements OnClickListener {
	private Activity mActivity;
	private ArrayList<Bitmap> mapList = new ArrayList<Bitmap>();//本地图片路径集合
	private String[] photo_items = new String[]{"选择本地图片", "拍照"};
	private RecyclerView mRecyclerView;
	private UpLoadAdapter mAdapter;
	private EditText name_edit,adress_text;
	private TextView Q_text;
	private String QrCode_Value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_layout);
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
		findViewById(R.id.sure).setOnClickListener(this);
		findViewById(R.id.cancel).setOnClickListener(this);
		findViewById(R.id.back_layout).setOnClickListener(this);

		name_edit = (EditText) findViewById(R.id.fragment_pet_name);
		adress_text = (EditText) findViewById(R.id.fragment_pet_adress);
		adress_text.setOnClickListener(this);
		Q_text = (TextView) findViewById(R.id.fragment_pet_QrCode);
		Q_text.setOnClickListener(this);
		name_edit.setText(MyManger.getUserInfo().getName());
		adress_text.setText(MyManger.getUserInfo().getAdress());
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

			case CaptureActivity.CAMERA_RESULT://二维码 回调
				switch (resultCode) {
					case CaptureActivity.CAMERA_RESULT:
						break;
					case CaptureActivity.PIC_RESULT:
						break;
				}
				if (data != null) {
//					QrCode_text.setText(data.getStringExtra("result"));
					QrCode_Value = data.getStringExtra("result");
					Q_text.setText("******");
					Q_text.setClickable(false);
				}
				break;

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
	 * 省市区 弹窗
	 */
	private void showDialog(){
//        //首先跳转到列表页面，通过startActivityForResult实现页面跳转传值
//        Intent intent = new Intent(IssueActivity.this, CityListSelectActivity.class);
//        startActivityForResult(intent, CityListSelectActivity.CITY_SELECT_RESULT_FRAG);

		CityPicker cityPicker = new CityPicker.Builder(this)
				.textSize(20)
				.title("丢失地址")
				.backgroundPop(0xa0000000)
				.titleBackgroundColor("#CCCCCC")
				.titleTextColor("#CCCCCC")
				.confirTextColor("#234Dfa")
				.cancelTextColor("#234Dfa")
				.province(MyManger.getCity(1,"北京市"))
				.city(MyManger.getCity(2,"北京市"))
				.district(MyManger.getCity(3,"朝阳区"))
				.textColor(Color.parseColor("#000000"))
				.provinceCyclic(true)
				.cityCyclic(false)
				.districtCyclic(false)
				.visibleItemsCount(7)
				.itemPadding(10)
				.onlyShowProvinceAndCity(false)
				.build();
		cityPicker.show();

		//监听方法，获取选择结果
		cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
			@Override
			public void onSelected(String... citySelected) {
				//省份
				String province = citySelected[0];
				//城市
				String city = citySelected[1];
				//区县（如果设定了两级联动，那么该项返回空）
				String district = citySelected[2];
				//邮编
				String code = citySelected[3];

				adress_text.setText(province+city+district);
			}

			@Override
			public void onCancel() {
//                Toast.makeText(IssueActivity.this, "已取消", Toast.LENGTH_LONG).show();
			}
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
        /*提交按钮*/
			case R.id.sure:
				uploadPic();
				break;
			case R.id.cancel:
			case R.id.back_layout:
				finish();
				break;
			case R.id.fragment_pet_QrCode:
				//打开扫描界面扫描条形码或二维码
                MainActivity.onPermissionRequests(Manifest.permission.CAMERA, new MainActivity.OnBooleanListener() {
                    @Override
                    public void onClick(boolean bln) {
                        if (bln) {
                            Intent openCameraIntent = new Intent(mActivity, CaptureActivity.class);
                            startActivityForResult(openCameraIntent, CaptureActivity.CAMERA_RESULT);
                        } else {
                            Toast.makeText(mActivity, "未打开相机权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
				break;
			case R.id.fragment_pet_adress:
				showDialog();
				break;
			default:
				break;
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
					changePetInfo(pic_map);
				}else{
					QINiuUtil.dismissDialog();
				}
			}
		});
	}


	private void changePetInfo(Map<String, String> pic_map){
		//改变宠物信息
		Map<String, String> map = new HashMap<>();
		map.put("userPhone", MyManger.getUserInfo().getPhone());
		map.put("patName", name_edit.getText().toString());
		map.put("homeAddress", adress_text.getText().toString());
		map.put("2dCode", MyManger.getQRCode());
		map.put("new2dCode", QrCode_Value);
		map.putAll(pic_map);
		RequestBody requestBody = RetroFactory.getIstance().getrequestBody(map);
		new RetroFitUtil<stringInfo>(this, RetroFactory.getIstance().getStringService().changePetInfo(requestBody))
				.request(new RetroFitUtil.ResponseListener<stringInfo>() {

					@Override
					public void onSuccess(stringInfo infos) {
						QINiuUtil.dismissDialog();
						if (!TextUtils.isEmpty(infos.getInfo())) {
							ToastUtil.showTextToast(getApplicationContext(),infos.getInfo().toString());
							UserInfo info = new UserInfo();
							info.setName(name_edit.getText().toString());
							info.setAdress(adress_text.getText().toString());
							MyManger.saveUserInfo(info);
							if(!TextUtils.isEmpty(QrCode_Value)){
								MyManger.saveQRCode(QrCode_Value);
							}
//							MyManger.savePicsArray(mAdapter.getList());
						} else {
							ToastUtil.showTextToast(getApplicationContext(),"二维码不合法");
//							ToastUtil.showTextToast(getApplicationContext(),infos.getErro());
						}
					}

					@Override
					public void onFail() {
						QINiuUtil.dismissDialog();
						ToastUtil.showTextToast(getApplicationContext(),getResources().getString(R.string.error_net));
					}

				});
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
											PhotoUtil.takePhotoForCamera(ChangePetActivity.this);
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
