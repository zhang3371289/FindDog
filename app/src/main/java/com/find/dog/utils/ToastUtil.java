package com.find.dog.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhangzhongwei on 2017/3/23.
 */

public class ToastUtil {
    private static Toast toast = null;

    /**
     * 自定义toast 显示最新toast不累加时间
     */
    public static void showTextToast(Context mContext, String msg) {
        if (toast == null) {
            toast = Toast.makeText(mContext, msg,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
