package com.vector.studynews.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by zhang on 2016/8/17.
 */
public class MyPageAdapter extends PagerAdapter {

    private List<ImageView> imageViewList;
    public MyPageAdapter(List<ImageView> imageViewList){
        this.imageViewList = imageViewList;
    }
    @Override
    public int getCount() {
        return imageViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViewList.get(position));
        return imageViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView(imageViewList.get(position));
    }

}
