package com.vector.studynews.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vector.studynews.R;

import java.util.zip.Inflater;

/**
 * Created by zhang on 2016/8/16.
 */
public class SubscribeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe,null);
        return view;
    }
}
