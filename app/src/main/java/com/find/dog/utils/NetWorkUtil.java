package com.find.dog.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.find.dog.R;
import com.find.dog.main.MyApplication;

public class NetWorkUtil
{
    /**
     * 判断网络连接状态
     * 
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)

        context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }
    public static boolean showNoIntentTaost() {
        if (NetWorkUtil.isNetworkAvailable(MyApplication.getInstance())) {
            return true;
        } else {
            ToastUtil.showTextToast(MyApplication.getInstance(),MyApplication.getInstance().getString(R.string.intent_no));
            return false;
        }
    }
}
