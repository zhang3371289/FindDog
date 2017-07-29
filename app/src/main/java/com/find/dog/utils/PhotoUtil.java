package com.find.dog.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.find.dog.main.MyApplication;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhangzhongwei on 2017/5/16.
 */

public class PhotoUtil {

    public static final int TAKE_PHOTO = 31;
    public static final int CHOOSE_PHOTO = 32;
    public static final int CROP_PHOTO = 33;
    public static String cachPath;
    private static File cacheFile;
    public static File cameraFile;
    private static Uri imageUri;
    private static Activity mContext;
    private static String image_name = "crop_image.jpg";

    /**
     * 打开相册
     *
     * @param context
     */
    public static void takePhotoForAlbum(Activity context) {
        init(context);
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        context.startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    /**
     * 打开相机
     *
     * @param context
     */
    public static void takePhotoForCamera(Activity context) {
        init(context);
        cameraFile = getCacheFile(new File(getDiskCacheDir(context)), "output_image.jpg");
        Log.e("H", "--cameraFile->" + cameraFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            imageUri = Uri.fromFile(cameraFile);
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imageUri = FileProvider.getUriForFile(context, "com.find.dog.fileprovider", cameraFile);
        }
        // 启动相机程序
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        context.startActivityForResult(intent, TAKE_PHOTO);
    }

    private static void init(Activity context){
//        /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
//        deleteFilesByDirectory(context.getCacheDir());

//        /**
//         * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
//         * context
//         */
//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            deleteFilesByDirectory(context.getExternalCacheDir());
//        }
        mContext = context;
        image_name = "crop_image_"+System.currentTimeMillis()+".jpg";
        cachPath = getDiskCacheDir(context) + "/"+ image_name;
        cacheFile = getCacheFile(new File(getDiskCacheDir(context)), image_name);
    }

    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    private static File getCacheFile(File parent, String child) {
        // 创建File对象，用于存储拍照后的图片
        File file = new File(parent, child);

        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    /**
     * 返回 图片地址
     * @param data
     * @return
     */
    public static String getPhotoPath(Intent data){
        String imagePath = "";
        Uri uri = data.getData();
        // 判断手机系统版本号
        if (Build.VERSION.SDK_INT >= 19) {
            // 4.4及以上系统使用这个方法处理图片
            imagePath = uriToPath(uri);
        } else {
            // 4.4以下系统使用这个方法处理图片
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }

    /**
     * 4.4及以上系统使用这个方法处理图片
     * @param uri
     * @return
     */
    @SuppressLint("NewApi")
    private static String uriToPath(Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(mContext, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            path = uri.getPath();
        }
        return path;
    }

    /**
     * 4.4以下系统使用这个方法处理图片
     * @param uri
     * @param selection
     * @return
     */
    private static String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = mContext.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 剪裁图片
     */
    public static void startPhotoZoom(File file) {
        Log.e("H", "裁剪照片的真实地址--->" + getImageContentUri(mContext, file));
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(mContext, file), "image/*");//自己使用Content Uri替换File Uri
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 450);//180
            intent.putExtra("outputY", 450);//180
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
//            image_name = "crop_image_"+System.currentTimeMillis()+".jpg";
//            initFile();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cacheFile));//定义输出的File Uri
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            mContext.startActivityForResult(intent, CROP_PHOTO);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

}
