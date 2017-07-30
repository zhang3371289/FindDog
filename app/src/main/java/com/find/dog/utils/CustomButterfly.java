package com.find.dog.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;

import com.find.dog.R;

public class CustomButterfly extends Dialog
{
    private static CustomButterfly customBUtton = null;

    public CustomButterfly(Context context)
    {
        super(context);
    }

    public CustomButterfly(Context context, int theme)
    {
        super(context, theme);
    }

    public static CustomButterfly getInstance(Context context) {
        if (customBUtton == null) {
            customBUtton = new CustomButterfly(context);
        }
        return customBUtton;
    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.butterflyImageView);
        // 获取ImageView上的动画背景
        AnimationDrawable spinner = (AnimationDrawable) imageView
                .getBackground();
        // 开始动画
        spinner.start();
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context
     *            上下文
     * @return
     */
    public static CustomButterfly show(Context context) {
        CustomButterfly mDialog = new CustomButterfly(context,
                R.style.Custom_Progress);
        //        CustomProgress mDialog = new CustomProgress(context);
        mDialog.setContentView(R.layout.spinner_butterfly);
        // 按返回键是否取消
        mDialog.setCancelable(true);
        // 设置居中
        Window window = mDialog.getWindow();
        window.getAttributes().gravity = Gravity.CENTER;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        mDialog.show();
        return mDialog;
    }

}
