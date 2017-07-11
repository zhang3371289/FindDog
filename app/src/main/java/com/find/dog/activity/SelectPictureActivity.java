package com.find.dog.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.find.dog.R;
import com.find.dog.image.ImageFolder;
import com.find.dog.image.ImageItem;
import com.find.dog.main.BaseActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * 图片多选（图片）
 * @author zlz
 * @Data 2016年7月26日
 */
public class SelectPictureActivity extends BaseActivity {

	/**
	 * 最多选择图片的个数
	 */
	private static int MAX_NUM = 3;
	private static final int TAKE_PICTURE = 520;

	public static final String INTENT_MAX_NUM = "intent_max_num";
	public static final String INTENT_SELECTED_PICTURE = "intent_selected_picture";

	private Context context;
	private GridView gridview;
	private PictureAdapter adapter;

//	private ImageLoader loader;
//	private DisplayImageOptions options;

	private Button btn_ok;
	private TextView selectSize;
	private ImageFolder currentImageFolder;

	/**
	 * 已选择的图片
	 */
	private ArrayList<String> selectedPicture = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.select_picture_activity);
		MAX_NUM = getIntent().getIntExtra(INTENT_MAX_NUM, 3);
		selectedPicture = getIntent().getStringArrayListExtra(INTENT_SELECTED_PICTURE);
//		loader = ImageLoader.getInstance();
//		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.list_default_bg)
//				.showImageForEmptyUri(R.drawable.list_default_bg).showImageOnFail(R.drawable.list_default_bg)
//				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
//				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();
		initView();


	}
	private void initView() {
		currentImageFolder = (ImageFolder) getIntent().getSerializableExtra("album");

		btn_ok = (Button) findViewById(R.id.btn_ok);
		selectSize = (TextView) findViewById(R.id.select_size_tv);
		selectSize.setText("已经选择("+selectedPicture.size() +"/"+ MAX_NUM+")");

		gridview = (GridView) findViewById(R.id.gridview);
		adapter = new PictureAdapter();
		gridview.setAdapter(adapter);


	}

	/**
	 * 点击完成按钮
	 * 
	 * @version 1.0
	 * @author zyh
	 * @param v
	 */
	public void ok(View v) {
		Intent data = new Intent();
		data.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
		setResult(RESULT_OK, data);
		this.finish();
	}
	/**
	 * 返回
	 * @param v
	 */
	public void back(View v) {
		onBackPressed();
	}
	class PictureAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return currentImageFolder.images.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.grid_item_picture, null);
				holder = new ViewHolder();
				holder.iv = (ImageView) convertView.findViewById(R.id.iv);
				holder.checkBox = (Button) convertView.findViewById(R.id.check);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.checkBox.setVisibility(View.VISIBLE);
			final ImageItem item = currentImageFolder.images.get(position);
			Glide.with(SelectPictureActivity.this).load(item.path).into(holder.iv);
//			loader.displayImage("file://" + item.path, holder.iv, options);
			boolean isSelected = selectedPicture.contains(item.path);
			holder.checkBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!v.isSelected() && selectedPicture.size() + 1 > MAX_NUM) {
						Toast.makeText(context, "最多选择" + MAX_NUM + "张", Toast.LENGTH_SHORT).show();
						return;
					}


					File srcFile = new File(item.path);
					int fileSize = 0;
					if(srcFile.exists()){
						fileSize = (int) (srcFile.length()/1024/1024);

					}
					if(fileSize > 7){
						Toast.makeText(context, "图片太大,暂无法上传~", Toast.LENGTH_SHORT).show();
						return;
					}

					if (selectedPicture.contains(item.path)) {
						selectedPicture.remove(item.path);
					} else {
						selectedPicture.add(item.path);
					}
					selectSize.setText("已经选择("+selectedPicture.size() +"/"+ MAX_NUM+")");
					v.setSelected(selectedPicture.contains(item.path));
				}
			});
			holder.checkBox.setSelected(isSelected);

			return convertView;
		}
	}

	class ViewHolder {
		ImageView iv;
		Button checkBox;
	}

}
