package com.find.dog.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.find.dog.R;

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
}
