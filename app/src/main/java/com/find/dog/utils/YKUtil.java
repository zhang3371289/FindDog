package com.find.dog.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.find.dog.main.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class YKUtil {
    private static final String CACHE_ROOT = "FindDog";

    /**
     * 将毫秒数转换成yyyy年MM月dd日 hh:mm的格式
     * @param timeStamp
     * @return
     */
    public static String getStrTime(String timeStamp){
        if(timeStamp.contains("-")){
            return timeStamp;
        }else {
            String timeString = null;
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long  time = Long.valueOf(timeStamp);
            timeString = sdf.format(new Date(time));//单位豪秒
            return timeString;
        }
    }

    /**
     * 返回unix时间戳 (1970年至今的豪秒数)
     * @return
     */
    public static long getUnixStamp(){
        return System.currentTimeMillis();
    }


    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param  （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将毫秒数转换成yyyy-MM-dd-HH-mm-ss的格式
     *
     * @param milliseconds
     * @return
     */
    private static String paserTime(long milliseconds) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String times = format.format(new Date(milliseconds));
        return times;
    }

    /**
     * 检测网络是否连接
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) MyApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            return manager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }


    // px转dp
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mWm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mWm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String getSDCardRootPath() {
        if (!hasSDCard()) {
            return null;
        }
        return Environment.getExternalStorageDirectory().toString();
    }

//    public static String getDownloadImagePath(Context context) {
//        boolean hasSDCard = hasSDCard();
//        if (hasSDCard) {
//            return getSDCardRootPath() + "/" + saveImage;
//        } else
//            return context.getCacheDir().getAbsoluteFile() + "/" + saveImage;
//
//    }
//
//    public static String getCacheRootDirPath(Context context) {
//        boolean hasSDCard = hasSDCard();
//        String root = null;
//        if (hasSDCard) {
//            root = getSDCardRootPath();
//        } else {
//            root = context.getCacheDir().getAbsoluteFile().toString();
//        }
//        String path = root + File.separator + CACHE_ROOT + File.separator;
//        return path;
//
//    }
//
//    public static String getDiskCachePath(Context context) {
//        String path = getCacheRootDirPath(context) + "cacheDir";
//        return path;
//    }
//
//    /**
//     * 保存获取的 软件信息，设备信息和出错信息保存在SDcard中
//     *
//     * @param context
//     * @param ex
//     * @return
//     */
//    public static String savaExceptionInfoToSD(Context context, Throwable ex) {
//        String fileName = null;
//        StringBuffer sb = new StringBuffer();
//
//        for (Map.Entry<String, String> entry : obtainSimpleInfo(context)
//                .entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            sb.append(key).append(" = ").append(value).append("\n");
//        }
//
//        sb.append(obtainExceptionInfo(ex));
//
//        File dir = new File(YKUtil.getCacheRootDirPath(context) + "crash"
//                + File.separator);
//        Log.d("Test", "--path--" + dir.getAbsolutePath());
//        if (dir.exists()) {
//            deleteFile(dir);
//        }
//        dir.mkdir();
//
//        try {
//            fileName = dir.toString() + File.separator
//                    + paserTime(System.currentTimeMillis()) + ".log";
//            FileOutputStream fos = new FileOutputStream(fileName);
//            fos.write(sb.toString().getBytes());
//            fos.flush();
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return fileName;
//    }
//
//    /**
//     * 获取系统未捕捉的错误信息
//     *
//     * @param throwable
//     * @return
//     */
//    public static String obtainExceptionInfo(Throwable throwable) {
//        StringWriter mStringWriter = new StringWriter();
//        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
//        throwable.printStackTrace(mPrintWriter);
//        mPrintWriter.close();
//
//        Log.e("YKUtil", mStringWriter.toString());
//        return mStringWriter.toString();
//    }
//    /**
//     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
//     *
//     * @param context
//     * @return
//     */
//    private static HashMap<String, String> obtainSimpleInfo(Context context) {
//        HashMap<String, String> map = new HashMap<String, String>();
//        PackageManager mPackageManager = context.getPackageManager();
//        PackageInfo mPackageInfo = null;
//        try {
//            mPackageInfo = mPackageManager.getPackageInfo(
//                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
//        } catch (NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        map.put("versionName", mPackageInfo.versionName);
//        map.put("versionCode", "" + mPackageInfo.versionCode);
//
//        map.put("MODEL", "" + Build.MODEL);
//        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
//        map.put("PRODUCT", "" + Build.PRODUCT);
//
//        return map;
//    }
//
//    /**
//     * 是否点击到View
//     *
//     * @param v
//     * @param event
//     * @return
//     */
//    public static boolean isHitView(View v, MotionEvent event) {
//        if (v == null || event == null) {
//            return false;
//        }
//        int[] leftTop = {0, 0};
//        v.getLocationInWindow(leftTop);
//        int left = leftTop[0];
//        int top = leftTop[1];
//        int bottom = top + v.getHeight();
//        int right = left + v.getWidth();
//        if (event.getX() > left && event.getX() < right && event.getY() > top
//                && event.getY() < bottom) {
//            return false;
//        }
//        return true;
//    }
//
//    public static void showKeyBoard(Context context, View view) {
//        InputMethodManager inputManager = (InputMethodManager) context
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        //        inputManager.showSoftInput(view, 0);
//        //        inputManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//    }
//
//    public static void hideKeyBoard(Context context, View view) {
//        InputMethodManager imm = (InputMethodManager) context
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }
//
//    public static void deleteFile(File file) {
//        if (file.isFile()) {
//            file.delete();
//            return;
//        }
//
//        if (file.isDirectory()) {
//            File[] childFiles = file.listFiles();
//            if (childFiles == null || childFiles.length == 0) {
//                return;
//            }
//            for (int i = 0; i < childFiles.length; i++) {
//                deleteFile(childFiles[i]);
//            }
//            file.delete();
//        }
//    }
//
//    public static boolean hasSdcard() {
//        String state = Environment.getExternalStorageState();
//        if (state.equals(Environment.MEDIA_MOUNTED)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//    /**
//     * 限制输入字节数
//     *
//     * @param inputStr
//     * @return
//     */
//    public static String getLimitSubstring(String inputStr, int size) {
//        if (null == inputStr) {
//            return "";
//        }
//        int orignLen = inputStr.length();
//        int resultLen = 0;
//        String temp = null;
//        for (int i = 0; i < orignLen; i++) {
//            temp = inputStr.substring(i, i + 1);
//            try {// 3 bytes to indicate chinese word,1 byte to indicate english
//                // word ,in utf-8 encode
//                if (temp.getBytes("utf-8").length == 3) {
//                    resultLen += 2;
//                } else {
//                    resultLen++;
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            /*限制510字节*/
//            if (resultLen > size * 2) {
//                return inputStr.substring(0, i);
//            }
//        }
//        return inputStr;
//    }
//
//    public static int getCharacterSize(String inputStr) {
//        if (null == inputStr) {
//            return 0;
//        }
//        int orignLen = inputStr.length();
//        int resultLen = 0;
//        String temp = null;
//        for (int i = 0; i < orignLen; i++) {
//            temp = inputStr.substring(i, i + 1);
//            try {// 3 bytes to indicate chinese word,1 byte to indicate english
//                // word ,in utf-8 encode
//                if (temp.getBytes("utf-8").length == 3) {
//                    resultLen += 2;
//                } else {
//                    resultLen++;
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//            return resultLen;
//
//        }
//        return 0;
//    }
//
//
//    public static Bitmap compressImage(Bitmap image) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 90, baos);
//        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while (baos.toByteArray().length / 1024 > 300) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            baos.reset();// 重置baos即清空baos
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;// 每次都减少10
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
//        return bitmap;
//    }
//
//    /**
//     * 检查权限  适配6.0
//     *
//     * @param context
//     * @param permission
//     * @return
//     */
//
//    public static boolean checkPermission(Context context, String permission) {
//        boolean result = false;
//        if (Build.VERSION.SDK_INT >= 23) {
//            try {
//                Class<?> clazz = Class.forName("android.content.Context");
//                Method method = clazz.getMethod("checkSelfPermission", String.class);
//                int rest = (Integer) method.invoke(context, permission);
//                if (rest == PackageManager.PERMISSION_GRANTED) {
//                    result = true;
//                } else {
//                    result = false;
//                }
//            } catch (Exception e) {
//                result = false;
//            }
//        } else {
//            PackageManager pm = context.getPackageManager();
//            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
//                result = true;
//            }
//        }
//        return result;
//    }
}
