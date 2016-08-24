package com.vector.studynews.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.vector.studynews.entity.Emojicon;

import java.util.List;

/**
 * Created by zhang on 2016/8/21.
 */
public class EmojiconPagerAdapter extends PagerAdapter {

    private List<View> views;

    public EmojiconPagerAdapter(List<View> views){
        this.views = views;
    }
    @Override
    public int getCount() {
        return views.size();
    }

    @Override

    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(views.get(arg1));
        return views.get(arg1);
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(views.get(arg1));

    }
}
