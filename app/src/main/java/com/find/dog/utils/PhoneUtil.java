package com.find.dog.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.find.dog.main.MyApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.sms.SMSSDK;
import cn.jpush.sms.listener.SmscheckListener;
import cn.jpush.sms.listener.SmscodeListener;

/**
 * Created by zhangzhongwei on 2017/4/27.
 */

public class PhoneUtil {
    private static MyCountDownTimer timer;
    private static PhoneUtil mInstance;
    private Context mContext;

    public static PhoneUtil getInstance() {
        if (mInstance == null) {
            mInstance = new PhoneUtil();
        }
        return mInstance;
    }

    public PhoneUtil() {
        mContext = MyApplication.getInstance();
        if (timer != null) {
            timer.cancel(); //防止new出多个导致时间跳动加速
            timer = null;
        }
    }

    /**
     * 验证手机号是否正确ֻ
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[0-9]|18[0-9])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

//    /**
//     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
//     * @param mobile 移动、联通、电信运营商的号码段
//     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
//     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
//     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
//     *<p>电信的号段：133、153、180（未启用）、189</p>
//     * @return 验证成功返回true，验证失败返回false
//     */
//    public static boolean isMobile(String mobile){
//        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
//        return Pattern.matches(regex, mobile);
//    }

    /**
     * 获取 验证码
     */
    public void getSmsCode(String mPhone, final Button yzmText) {
        if (!yzmText.getText().equals("获取验证码")) {
            return;
        }
        timer = new MyCountDownTimer(60 * 1000, 1000, yzmText);
        if (isMobile(mPhone)) {
            SMSSDK.getInstance().getSmsCodeAsyn(mPhone, "1", new SmscodeListener() {
                @Override
                public void getCodeSuccess(final String uuid) {
                    timer.start();
//                    ToastUtil.showTextToast(mContext, uuid);
                }

                @Override
                public void getCodeFail(int errCode, final String errmsg) {
                    //失败后停止计时
                    timer.cancel();
                    ToastUtil.showTextToast(mContext, errmsg);
                }
            });
        } else {
            ToastUtil.showTextToast(mContext, "手机号码格式不正确");
        }

    }


    /**
     * 验证 验证码
     */
    public void checkSmsCode(String mPhone, String code, final PhoneCallback mCallback) {
//        if (mCallback != null) {
//            mCallback.onSuccess(code);
//        }
        SMSSDK.getInstance().checkSmsCodeAsyn(mPhone, code, new SmscheckListener() {
            @Override
            public void checkCodeSuccess(final String code) {
//                ToastUtil.showTextToast(mContext, code);
                if (mCallback != null) {
                    mCallback.onSuccess(code);
                }
            }

            @Override
            public void checkCodeFail(int errCode, final String errmsg) {
                ToastUtil.showTextToast(mContext, errmsg);
            }
        });
    }

    /**
     * 发送验证码计时
     * 继承 CountDownTimer 防范
     * <p>
     * 重写 父类的方法 onTick() 、 onFinish()
     */

    private class MyCountDownTimer extends CountDownTimer {
        TextView yzmText;

        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          <p>
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          <p>
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval, Button text) {
            super(millisInFuture, countDownInterval);
            this.yzmText = text;
        }

        @Override
        public void onFinish() {
            yzmText.setText("获取验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            yzmText.setText("倒计时(" + millisUntilFinished / 1000 + "秒)");
        }
    }

    /**
     * 验证 验证码
     *
     * @param <T>
     */
    public static abstract class PhoneCallback<T> {
        /**
         * @param t
         */
        public abstract void onSuccess(T t);
    }

}
