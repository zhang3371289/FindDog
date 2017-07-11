package com.find.dog.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;

import com.find.dog.R;
import com.find.dog.main.MyApplication;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @ClassName BitmapUtilImage
 * @Description 图片工具类
 * @author huangke@yoka.com
 * @date 2011-12-26 下午04:21:07
 * 
 */
public class BitmapUtilImage
{

	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Context context, Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		Bitmap output = null;
		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int left = 0, top = 0, right = width, bottom = height;
			float roundPx = height / 2;
			if (width > height) {
				left = (width - height) / 2;
				top = 0;
				right = left + height;
				bottom = height;
			} else if (height > width) {
				left = 0;
				top = (height - width) / 2;
				right = width;
				bottom = top + width;
				roundPx = width / 2;
			}
			output = Bitmap
					.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			int color = 0xff424242;
			Paint paint = new Paint();
			Rect rect = new Rect(left, top, right, bottom);
			RectF rectF = new RectF(rect);

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			bitmap.recycle();
		} catch (Throwable e) {
			e.printStackTrace();
			System.gc();
		}
		if (output == null) {
			output = BitmapFactory.decodeResource(context.getResources(),
					R.color.c_8899a6);
		}
		System.out.println("xxx  toRoundCorner   "+output.getWidth()+"    "+output.getHeight());
		return output;

	}
	/**
	 * 根据图片修改显示尺寸
	 * @param imageWidth
	 * @param imageHeight
	 * @return
	 */
	public static int[] getImageSize(int imageWidth,int imageHeight){
		int size[]=new int[2];
		WindowManager wm = (WindowManager) MyApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = wm.getDefaultDisplay().getWidth();
		if (imageWidth == 0) {
			imageWidth = 200;
		} else if (imageHeight == 0) {
			imageHeight = 200;
		}
		int tmpHeight = screenWidth * imageHeight / imageWidth;
		size[0]=screenWidth;
		size[1]=tmpHeight;
		return size;
	}

	private static File tempFile;

	//Bitmap对象保存味图片文件
	public static String saveBitmapFile(Bitmap bitmap){
		tempFile = new File(Environment.getExternalStorageDirectory() + "/",getPhotoFileName());//将要保存图片的路径
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tempFile.getPath();
	}

	@SuppressLint("SimpleDateFormat")
	private static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}


	public static int[] BitmapSiz(String path){
		int []n=new int[2];
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);
		int width = options.outWidth;
		int height = options.outHeight;

		n[0]=width;
		n[1]=height;
		return n;
	}
	//质量压缩方法
	private static Bitmap compressImage(Bitmap image, String path) {
		if(image != null){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BitmapFactory.Options optionsSize =new BitmapFactory.Options();
			optionsSize.inJustDecodeBounds = false;
			optionsSize.inSampleSize = 7;   //width，hight设为原来的十分一
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;  
			while ( baos.toByteArray().length / 1024 > 4096) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
				baos.reset();//重置baos即清空baos  
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
				options -= 10;//每次都减少10  
			}  
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, optionsSize);//把ByteArrayInputStream数据生成图片

			int orientation = BitmapUtilImage.readPictureDegree(path);//获取旋转角度
			if(Math.abs(orientation) > 0){
				bitmap =  BitmapUtilImage.rotaingImageView(orientation, bitmap);//旋转图片
			}
			return bitmap;  
		}  
		return null;  
	}
	//图片按比例大小压缩方法（根据路径获取图片并压缩）
	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		Bitmap bitmap = null;
		//开始读入图片，此时把options.inJustDecodeBounds 设回true了  
		newOpts.inJustDecodeBounds = true;  
		newOpts.inSampleSize = 2;
		try {
			bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空


		} catch (OutOfMemoryError e) {
			return null;
		}

		newOpts.inJustDecodeBounds = false;  
		int w = newOpts.outWidth;  
		int h = newOpts.outHeight;  
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
		float hh = 800f;//这里设置高度为800f  
		float ww = 480f;//这里设置宽度为480f  
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
		int be = 1;//be=1表示不缩放  
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
			be = (int) (newOpts.outWidth / ww);  
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
			be = (int) (newOpts.outHeight / hh);  
		}  
		//	        if (be <= 0)  
		//	            be = 5;  
		//	        newOpts.inSampleSize = be;//设置缩放比例  
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
		try {
			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			return null;
		}
		//		return compressImage(bitmap,srcPath);//压缩好比例大小后再进行质量压缩  
		return bitmap;//压缩好比例大小后再进行质量压缩  
	} 

	/**
	 * 读取图片度数
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	/**
	 * 旋转图片
	 * @param angle
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {

		//旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		matrix.postScale(0.5f, 0.5f);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		//		Bitmap img2 = resizedBitmap.copy(Bitmap.Config.ARGB_4444, false);//获得ARGB_4444色彩模式的图片  

		bitmap = null;
		//		resizedBitmap = null;
		return resizedBitmap;
	}

	public static Bitmap getLocationBitmap(String url){
		int size = getZoomSize(url);
		Bitmap bitmap = null;
		//GT-N7102三星
		/*设备型号*/
		String phonemodel = YKDeviceInfo.getDeviceModel();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(url);

			BitmapFactory.Options options = new  BitmapFactory.Options();

			options.inJustDecodeBounds =  false;
			/*if(!phonemodel.equals("GT-N7102") && !url.contains("Camera")){
				options.inPreferredConfig = Bitmap.Config.ARGB_4444;
			}*/

			options.inSampleSize =  size;   // width，hight设为原来的十分一
			int orientation = BitmapUtilImage.readPictureDegree(url);//获取旋转角度

			bitmap =  BitmapFactory.decodeStream(fis, null,  options);
			if(Math.abs(orientation) > 0){
				bitmap =  BitmapUtilImage.rotaingImageView(orientation, bitmap);//旋转图片
			}
			return bitmap;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}
	public static int getZoomSize(String url){
		int size = 1;
		int fileSize = 0;

		//GT-N7102三星
		/*设备型号*/
		String phonemodel = YKDeviceInfo.getDeviceModel();


		File srcFile = new File(url);
		if(srcFile.exists()){
			fileSize = (int) (srcFile.length()/1024/1024);
			Log.d("Aenior", "size"+fileSize);
		}



		if(fileSize <= 4){
			if(phonemodel.equals("GT-N7102")){
				if(url.contains("Camera")){
					size = 5;
				}else{
					size = 2;
				}

			}else if(url.contains("Camera")){
				size = 6;
			}else{
				size = 2;
			}
		}else if(4 < size && size < 7){
			if(phonemodel.equals("GT-N7102")){
				if(url.contains("Camera")){
					size = 7;
				}else{
					size = 3;
				}
			}else if(url.contains("Camera")){
				size = 6;
			}else{
				size = 2;
			}
		}else{
			size = 10;
		}


		return size;
	}
}

