package com.find.dog.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.find.dog.R;
import com.find.dog.image.ImageFolder;
import com.find.dog.image.ImageItem;
import com.find.dog.main.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 图片多选（相册）
 * @author zlz
 * @Data 2016年7月26日
 */
public class SelectAlbumActivity extends BaseActivity {

	private FolderAdapter folderAdapter;
	private ImageFolder currentImageFolder;
	private ListView listview;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();
	private ArrayList<ImageFolder> mDirPaths = new ArrayList<ImageFolder>();

//	private ImageLoader loader;
	//	private DisplayImageOptions options;
	private ContentResolver mContentResolver;

	//	private int maxSize;
	private ArrayList<String> selected;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_album_activity);
		mContentResolver = getContentResolver();
		//		loader = ImageLoader.getInstance();
		//		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.list_default_bg)
		//				.showImageForEmptyUri(R.drawable.list_default_bg).showImageOnFail(R.drawable.list_default_bg)
		//				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
		//				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();

		initView();

	}

	private void initView() {
		//		maxSize = getIntent().getIntExtra(SelectPictureActivity.INTENT_MAX_NUM, 3);
		selected = getIntent().getStringArrayListExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);

		listview = (ListView) findViewById(R.id.listview);
		folderAdapter = new FolderAdapter();
		listview.setAdapter(folderAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				currentImageFolder = mDirPaths.get(position);
				Log.d("zyh", position + "-------" + currentImageFolder.getName() + "----"
						+ currentImageFolder.images.size());

				//TODO : 点击跳转相册详情图片页
				Intent intent = new Intent(SelectAlbumActivity.this,SelectPictureActivity.class);
				intent.putExtra("album", currentImageFolder);
				//				intent.putExtra(SelectPictureActivity.INTENT_MAX_NUM, max);//选择三张
				intent.putExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE, selected);
				startActivityForResult(intent,22);

			}
		});

		getThumbnail();
	}

	public void back(View v){
		finish();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case RESULT_OK://相册选择返回
			setResult(RESULT_OK, data);
			finish();
			break;

		default:
			break;
		}
	}


	/**
	 * 得到缩略图
	 */
	private void getThumbnail() {
		Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.ImageColumns.DATA }, "", null,
				MediaStore.MediaColumns.DATE_ADDED + " DESC");
//		Log.d("TAG", mCursor.getCount() + "");
		if (mCursor.moveToFirst()) {
			int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
			do {
				// 获取图片的路径
				String path = mCursor.getString(_date);
//				Log.d("TAG", path);
				// 获取该图片的父路径名
				File parentFile = new File(path).getParentFile();
				if (parentFile == null) {
					continue;
				}
				ImageFolder imageFloder = null;
				String dirPath = parentFile.getAbsolutePath();
				if (!tmpDir.containsKey(dirPath)) {
					// 初始化imageFloder
					imageFloder = new ImageFolder();
					imageFloder.setDir(dirPath);
					imageFloder.setFirstImagePath(path);
					mDirPaths.add(imageFloder);
					Log.d("zyh", dirPath + "," + path);
					tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
				} else {
					imageFloder = mDirPaths.get(tmpDir.get(dirPath));
				}
				imageFloder.images.add(new ImageItem(path));
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		for (int i = 0; i < mDirPaths.size(); i++) {
			ImageFolder f = mDirPaths.get(i);
			Log.d("zyh", i + "-----" + f.getName() + "---" + f.images.size());
		}
		tmpDir = null;
	}

	class FolderAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDirPaths.size();
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
			FolderViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(SelectAlbumActivity.this, R.layout.list_dir_item, null);
				holder = new FolderViewHolder();
				holder.id_dir_item_image = (ImageView) convertView.findViewById(R.id.id_dir_item_image);
				holder.id_dir_item_name = (TextView) convertView.findViewById(R.id.id_dir_item_name);
				holder.id_dir_item_count = (TextView) convertView.findViewById(R.id.id_dir_item_count);
				holder.choose = (ImageView) convertView.findViewById(R.id.choose);
				convertView.setTag(holder);
			} else {
				holder = (FolderViewHolder) convertView.getTag();
			}
			ImageFolder item = mDirPaths.get(position);

			//			try {
			Glide.with(SelectAlbumActivity.this).load(item.getFirstImagePath()).into(holder.id_dir_item_image);
//			loader.displayImage("file://" + item.getFirstImagePath(), holder.id_dir_item_image, options);
			//			} catch (Exception e) {
			//
			//				Log.e("SelectAlbumActivity", "---"+e.getMessage().toString());
			//			}

			holder.id_dir_item_count.setText(item.images.size() + "张");
			holder.id_dir_item_name.setText(item.getName());
			holder.choose.setVisibility(currentImageFolder == item ? 0 : 8);
			return convertView;
		}
	}

	class FolderViewHolder {
		ImageView id_dir_item_image;
		ImageView choose;
		TextView id_dir_item_name;
		TextView id_dir_item_count;
	}
	@Override
	public void onDestroy() {  
		super.onDestroy(); 
//		loader.clearMemoryCache();  
//		loader.clearDiskCache();  
	}  






}
