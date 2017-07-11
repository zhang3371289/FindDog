package com.find.dog.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BitmapUtil
{

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param imagePath
	 *            图像的路径
	 * @param width
	 *            指定输出图像的宽度
	 * @param height
	 *            指定输出图像的高度
	 * @return 生成的缩略图
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width,
										   int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 写图片文件到SD卡
	 * 
	 * @throws IOException
	 */
	public static void saveImageToSD(Context ctx, String filePath,
									 Bitmap bitmap, int quality) throws IOException {
		if (bitmap != null) {
			File file = new File(filePath.substring(0,
					filePath.lastIndexOf(File.separator)));
			if (!file.exists()) {
				file.mkdirs();
			} else {
				file.delete();
				file.mkdir();
			}
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(filePath));
			bitmap.compress(CompressFormat.JPEG, quality, bos);
			try {
				bos.flush();
			} catch (Exception e) {
				bitmap.recycle();
				bitmap=null;
			}
			try {
				bos.close();
			} catch (Exception e) {
				bitmap.recycle();
				bitmap=null;
			}
		}
		bitmap.recycle();
		bitmap=null;
	}

	private static String getDiskCacheDir(Context context) {
		String cachePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return cachePath;
	}

	/**
	 * 保存图片
	 * 
	 * @param bitName
	 * @param mBitmap
	 */
	public static String saveMyBitmap(String bitName, Bitmap mBitmap, Context context) {
//		File f = new File("/sdcard/" + bitName + ".png");
		File f = new File(getDiskCacheDir(context) + bitName + ".png");
		try {
			f.createNewFile();
		} catch (IOException e) {
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		mBitmap.compress(CompressFormat.PNG, 100, fOut);

		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f.getPath();
	}

	/**
	 * 根据路径获取图片
	 *
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmapByPath(String filePath) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis, null, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	/**
	 * 通过路径读取图片
	 *
	 * locImgUrl为所读取图片的路径
	 * @return
	 */
	public static Bitmap getSDimg(String locImgUrl) {
		Bitmap bitmap = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File mfile;
			mfile = new File(locImgUrl);
			if (mfile.exists()) {// 若该文件存在
				bitmap = BitmapFactory.decodeFile(locImgUrl);
			}
		}
		return bitmap;
	}

	/*
	 * 将图像保存到Data目录 注意：由于程序数据空间有限，故请不要保存流媒体及大量数据，否则会导致其它应用程序无法安装或运行。
	 */
	/**
	 *
	 * @param act
	 *            写入 data 的位图
	 * @param bmpToSave
	 *            调用的 Activity
	 * @param FileNameWithoutExtension
	 *            文件名（不含扩展名）
	 * @param ext
	 *            扩展名
	 * @param Quality
	 *            图像质量
	 * @return
	 */
	public static boolean saveBitmapToData(Activity act, Bitmap bmpToSave,
										   String FileNameWithoutExtension, String ext, int Quality) {
		try {
			if (Quality > 100)
				Quality = 100;
			else if (Quality < 1)
				Quality = 1;

			FileOutputStream fos = act.openFileOutput(FileNameWithoutExtension
					+ "." + ext, Context.MODE_PRIVATE); // 这里是关键，其实就是一个不含路径但包含扩展名的文件名

			bmpToSave.compress(CompressFormat.PNG, Quality, fos);

			// 写入文件
			fos.flush();
			fos.close();

			return true;
		} catch (Exception e) {
			if (e.getMessage() != null)
				Log.w("Util_SaveBitmapToData", e.getMessage());
			else
				e.printStackTrace();

			return false;
		}
	}

	/* 从Data目录读取图像 */
	public static Bitmap getBitmapFromData(Activity act, String FileName) {
		FileInputStream fis = null;
		try {
			fis = act.openFileInput(FileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedInputStream bis = new BufferedInputStream(fis);
		Bitmap bmpRet = BitmapFactory.decodeStream(bis);

		try {
			bis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bmpRet;
	}

	// 使用系统当前日期加以调整作为照片的名称
	@SuppressLint("SimpleDateFormat")
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
}
