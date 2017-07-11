package com.find.dog.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.find.dog.R;

/**
 * Created by zhangzhongwei on 2017/6/27.
 * 社区Fragment
 */

public class CommunityFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
        intview(rootView);
        return rootView;
    }

    private void intview(View rootView) {

    }
}
