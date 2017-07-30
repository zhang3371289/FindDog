package com.find.dog.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.find.dog.main.MyApplication;
import com.qiniu.android.common.AutoZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangzhongwei on 2017/7/27.
 */

public class QINiuUtil {
    final String photo_value = "http://otmv1mqzg.bkt.clouddn.com/";
    public static String photo_suffix= "?imageMogr2/thumbnail/500x/strip/quality/50/format/webp";
    private static QINiuUtil mInstance;
    private static Context mContext;
    private static ProgressDialog progressDialog;
    private boolean isProgressCancel;  //网络请求过程中是否取消上传或下载
    private UploadManager uploadManager;  //七牛SDK的上传管理者
    private UploadOptions uploadOptions;  //七牛SDK的上传选项
    private UpProgressHandler upProgressHandler;  //七牛SDK的上传进度监听
    private UpCancellationSignal upCancellationSignal;  //七牛SDK的上传过程取消监听
    private int list_length = 0;
    private Map<String, String> key_map = new HashMap<>();//
    double percent0 , percent1 , percent2 ;
//    String key = "";   //<指定七牛服务上的文件名，或 null>;
//    String token = ""; //<从服务端SDK获取>;

    public static QINiuUtil getInstance() {
        if (mInstance == null) {
            mInstance = new QINiuUtil();
        }
        return mInstance;
    }

    public QINiuUtil() {
        //FixedZone.zone0   华东机房
        //FixedZone.zone1   华北机房
        //FixedZone.zone2   华南机房
        //FixedZone.zoneNa0 北美机房
        //自动识别上传区域
        //AutoZone.autoZone
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
//                .recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
//                .recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(AutoZone.autoZone)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);
//        Log.e("H", "---------QINiuUtil-----------");

        upProgressHandler = new UpProgressHandler() {
            /**
             * @param key 上传时的upKey；
             * @param percent 上传进度；
             */
            @Override
            public void progress(String key, double percent) {

                String result = key.substring(key.indexOf("_")+1, key.indexOf("."));
                if("0".equals(result)){
                    percent0 = percent;
                }if("1".equals(result)){
                    percent1 = percent;
                }
                if("2".equals(result)){
                    percent2 = percent;
                }
//                progressDialog.setProgress((int) ((list_length+1) * percent));
                progressDialog.setProgress((int) ((percent0+percent1+percent2)*100/list_length));
//                Log.e("H",percent0+"-----"+percent1+"-----------"+percent2);
//                Log.e("H",result+"---percent--"+(percent0+percent1+percent2)*100/list_length+"-----------"+percent0+percent1+percent2);
            }
        };
        upCancellationSignal = new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                return isProgressCancel;
            }
        };
        //定义数据或文件上传时的可选项
        uploadOptions = new UploadOptions(
                null,  //扩展参数，以<code>x:</code>开头的用户自定义参数
                "mime_type",  //指定上传文件的MimeType
                true,  //是否启用上传内容crc32校验
                upProgressHandler,  //上传内容进度处理
                upCancellationSignal  //取消上传信号
        );
    }

    /**
     * 上传图片
     *
     * @param mtempList <File对象、或 文件路径、或 字节数组>
     */
    public void uploadPic(Context mContext,final ArrayList<String> mtempList, final Callback callback) {
        this.mContext = mContext;
        key_map = new HashMap<>();
        String token = MyManger.getQiNiuToken();
        list_length = mtempList.size();
        if(list_length <= 0){
            ToastUtil.showTextToast(MyApplication.getInstance(), "未添加图片");
            if (callback != null) {
                callback.callback(false,key_map);
            }
            return;
        }
        initProgressBar();
        for (int i = 0; i < list_length; i++) {
            String key = MyManger.getUserInfo().getPhone() + YKUtil.getUnixStamp() + "_" + i + ".jpg";
            final String photo_key = "photo"+ (i+1) +"URL";
            uploadManager.put(mtempList.get(i), key, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                            if (info.isOK()) {
                                Log.i("qiniu", "Upload Success");
                                if (res != null) {
                                    try {
                                        String key_back = res.getString("key");
                                        key_map.put(photo_key, photo_value+key_back);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {
                                Log.i("qiniu", "Upload Fail");
                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
//                                ToastUtil.showTextToast(MyApplication.getInstance(), info.error);
                                if (callback != null) {
                                    callback.callback(false,key_map);
                                }
                            }
                            if (callback != null && key_map.size() == list_length) {
                                callback.callback(true,key_map);
                            }
                            Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                        }
                    }, uploadOptions);
        }

    }

    public interface Callback {
        public void callback(boolean isOk,Map<String, String> pic_map);
    }

    private void initProgressBar() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("资料上传中");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                isProgressCancel = true;
//            }
//        });

        progressDialog.setMax(100);
        progressDialog.show();
        isProgressCancel = false;
        percent0 = percent1 = percent2 = 0;
    }

    /**
     * 等待Dialog
     */
    public static void dismissDialog() {
        if (progressDialog == null) {
            return;
        }
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
        }
    }
}
