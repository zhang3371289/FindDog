package com.find.dog.utils;

import android.util.Log;

import com.find.dog.main.MyApplication;
import com.qiniu.android.common.AutoZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangzhongwei on 2017/7/27.
 */

public class QINiuUtil {
    final String photo_value = "http://otmv1mqzg.bkt.clouddn.com/";
    public static String photo_suffix= "?imageMogr2/thumbnail/500x/strip/quality/50/format/webp";
    private static QINiuUtil mContext;
    private UploadManager uploadManager;
    private boolean isOk;
//    String key = "";   //<指定七牛服务上的文件名，或 null>;
//    String token = ""; //<从服务端SDK获取>;

    public static QINiuUtil getInstance() {
        if (mContext == null) {
            mContext = new QINiuUtil();
        }
        return mContext;
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
    }

    /**
     * 上传图片
     *
     * @param mtempList <File对象、或 文件路径、或 字节数组>
     */
    public void uploadPic(final ArrayList<String> mtempList, final Callback callback) {
        final Map<String, String> map = new HashMap<>();
        String token = MyManger.getQiNiuToken();
        final int length = mtempList.size();
        if(length == 0){
            ToastUtil.showTextToast(MyApplication.getInstance(), "未添加图片");
            if (callback != null) {
                callback.callback(isOk,map);
            }
        }
        for (int i = 0; i < length; i++) {
            String key = MyManger.getUserInfo().getPhone() + YKUtil.getUnixStamp() + "_" + i + ".jpg";
            final String photo_key = "photo"+ (i+1) +"URL";
            uploadManager.put(mtempList.get(i), key, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                            if (info.isOK()) {
                                isOk = true;
                                Log.i("qiniu", "Upload Success");
                                if (res != null) {
                                    try {
                                        String key_back = res.getString("key");
                                        map.put(photo_key, photo_value+key_back);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {
                                Log.i("qiniu", "Upload Fail");
                                isOk = false;
                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
//                                ToastUtil.showTextToast(MyApplication.getInstance(), info.error);
                                if (callback != null) {
                                    callback.callback(isOk,map);
                                }
                            }
                            if (callback != null && map.size() == length) {
                                callback.callback(isOk,map);
                            }
                            Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                        }
                    }, null);
        }

    }

    public interface Callback {
        public void callback(boolean isOk,Map<String, String> pic_map);
    }

}
