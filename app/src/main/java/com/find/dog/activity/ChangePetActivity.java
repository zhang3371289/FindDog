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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.find.dog.R;
import com.find.dog.adapter.UpLoadAdapter;
import com.find.dog.image.BitmapUtil;
import com.find.dog.main.BaseActivity;
import com.find.dog.utils.BitmapUtilImage;
import com.find.dog.utils.MyManger;
import com.find.dog.utils.PhotoUtil;
import com.find.dog.utils.ToastUtil;
import com.google.zxing.activity.CaptureActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * 修改信息
 */
public class ChangePetActivity extends BaseActivity implements OnClickListener {
	private Activity mActivity;
	private ArrayList<Bitmap> mapList = new ArrayList<Bitmap>();//本地图片路径集合
	private ArrayList<String> mtempList = new ArrayList<String>();//压缩后图片路径集合
	private String mGetEditText = "";
	private Bitmap tempBitmap;
	private String[] photo_items = new String[]{"选择本地图片", "拍照"};
	private RecyclerView mRecyclerView;
	private UpLoadAdapter mAdapter;
	private EditText name_edit,adress_edit;
	private TextView QrCode_text;
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
		adress_edit = (EditText) findViewById(R.id.fragment_pet_adress);
		QrCode_text = (TextView) findViewById(R.id.fragment_pet_QrCode);
		QrCode_text.setOnClickListener(this);
		name_edit.setText(MyManger.getUserInfo().getName());
		adress_edit.setText(MyManger.getUserInfo().getAdress());
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
					QrCode_text.setText(data.getStringExtra("result"));
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
			case R.id.sure:
				ToastUtil.showTextToast(this, "确定");
				break;
			case R.id.cancel:
			case R.id.back_layout:
				finish();
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

	}


	/**
	 * 请求网络 提交回复
	 */
	private void commitReplay() {


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

}
