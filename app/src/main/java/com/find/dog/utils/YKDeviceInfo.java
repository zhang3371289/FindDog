package com.find.dog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.text.TextUtils;

import java.util.UUID;

public class YKDeviceInfo
{
    private static Context context;
    private static String clientID = null;

    public static void init(Context c) {
        context = c;
        clientID = YKDeviceInfo.getLocalClientId(c);
    }

    public static String getUserAgent() {
        return "MrSkin/" + YKDeviceInfo.getAppVersion() + " (YOKA;"
                + YKDeviceInfo.getDeviceModel() + ";"
                + YKDeviceInfo.getOSVersion() + ")";
    }

    public static String getClientID() {
        if (TextUtils.isEmpty(clientID)) {
            String clientId = getLocalClientId(context);
            if (TextUtils.isEmpty(clientId)) {
                clientId = UUID.randomUUID().toString();
                saveLocalClientId(context, clientId);
                clientID = clientId;
            }
        }
        return clientID;
    }

    public static String getAppVersion() {
        if (context == null)
            return "";
        String tmpAppVersion = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            tmpAppVersion = info.versionName;
        } catch (Exception e) {
        }
        return tmpAppVersion;
    }

    public static String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    public static String getOSVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

//    public static String getScreenWidth() {
//        return "" + YKUtil.getScreenWidth(context);
//    }
//
//    public static String getScreenHeight() {
//        return "" + YKUtil.getScreenHeight(context);
//    }
//
//    public static float getScreenWid() {
//        return YKUtil.getScreenWidth(context);
//    }

    public static void saveLocalClientId(Context context, String clientId) {
        Editor edit = getSharedPreferencesEditor(context);
        edit.putString(STORE_KEY_CLIENTID, clientId);
        edit.commit();
    }

    private static String getLocalClientId(Context context) {
        SharedPreferences content = getSharedPreferences(context);
        return content.getString(STORE_KEY_CLIENTID, null);
    }

    // =======================================
    private final static String STORE_NAME = "YOKA_DATA";
    private final static String STORE_KEY_CLIENTID = "clientid";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(STORE_NAME, 0);
    }

    private static Editor getSharedPreferencesEditor(Context context) {
        return getSharedPreferences(context).edit();
    }
}
