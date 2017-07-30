package com.find.dog.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.SystemClock;

/**
 * Created by ZhangV on 2017/7/30.
 */

public class DialogUtil {
    private static ProgressDialog progressDialog;
//    private static DialogUtil mContext;
//    public static DialogUtil getInstance() {
//        if (mContext == null) {
//            mContext = new DialogUtil();
//        }
//        return mContext;
//    }

    /**
     * 等待Dialog
     */
    public static void showWaitingDialog(Context context,int value) {
       /* 等待Dialog具有屏蔽其他控件的交互能力
        * setCancelable 为使屏幕不可点击，设置为不可取消(false)
        * 下载等事件完成后，主动调用函数关闭该Dialog
        * 进度条是否明确 setIndeterminate(boolean b);
        * 不明确就是滚动条的当前值自动在最小到最大值之间来回移动，形成这样一个动画效果，
        * 这个只是告诉别人“我正在工作”，但不能提示工作进度到哪个阶段。
        * 主要是在进行一些无法确定操作时间的任务时作为提示。
        * 而“明确”就是根据你的进度可以设置现在的进度值。
        */
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("数据上传中");
        progressDialog.setMessage(value * 100 +"%...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
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
