package com.find.dog.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.find.dog.R;
import com.find.dog.activity.MainActivity;
import com.find.dog.main.MyApplication;

/**
 * Created by zhangzhongwei on 2017/6/27.
 * 发现Fragment
 */

public class FindFragment extends Fragment {
    private Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find, container, false);
        mContext = getActivity();
        intview(rootView);
        return rootView;
    }

    private void intview(View rootView) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            MainActivity.onPermissionRequests(Manifest.permission.ACCESS_FINE_LOCATION, new MainActivity.OnBooleanListener() {
                @Override
                public void onClick(boolean bln) {
                    if (bln) {
                        MyApplication.getInstance().mLocationClient.start();
                    } else {
                        Toast.makeText(MyApplication.getInstance(), "文件读写或无法正常使用", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            MyApplication.getInstance().mLocationClient.stop();
        }
    }

}
