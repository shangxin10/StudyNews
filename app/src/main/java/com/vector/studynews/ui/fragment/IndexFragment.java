package com.vector.studynews.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vector.studynews.R;
import com.vector.studynews.adapter.ArticleAdapter;
import com.vector.studynews.adapter.MyPageAdapter;
import com.vector.studynews.config.Config;
import com.vector.studynews.entity.Article;
import com.vector.studynews.listener.OnRefreshListener;
import com.vector.studynews.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhang on 2016/8/16.
 */
public class IndexFragment extends Fragment {
    public static String TAG ="IndexFragment";
    public static final int SLIDER_MEG = 1;
    public static final int NEWS_REFRESH =2;
    public static final int NEWS_LOADMORE = 3;

    private RefreshListView ll_content;
    private ViewPager vp_slider;
    private List<ImageView> imageViewList;
    private ImageView imageView1,imageView2,imageView3;
    private int currentItem=0;


    //轮播时间
    public ScheduledExecutorService scheduledExecutorService;
    public SliderTask  sliderTask;
    public boolean sliderState = false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what== SLIDER_MEG){
                vp_slider.setCurrentItem(currentItem);
            }else if(msg.what == NEWS_REFRESH){
                ll_content.hideHeadView();
            }else if(msg.what == NEWS_LOADMORE){
                ll_content.hideFooterView();
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index,null);
        ll_content = (RefreshListView) view.findViewById(R.id.ll_content);
        vp_slider = (ViewPager) view.findViewById(R.id.vp_slider);
        imageViewList = new ArrayList<ImageView>();

        imageView1 = new ImageView(getContext());
        imageView1.setImageResource(R.mipmap.slider1);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageViewList.add(imageView1);
        imageView2 = new ImageView(getContext());
        imageView2.setImageResource(R.mipmap.slider2);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageViewList.add(imageView2);

        imageView3 = new ImageView(getContext());
        imageView3.setImageResource(R.mipmap.slider3);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageViewList.add(imageView3);

        MyPageAdapter myPageAdapter = new MyPageAdapter(imageViewList);
        vp_slider.setAdapter(myPageAdapter);

        vp_slider.setOnPageChangeListener(new SliderPageChangeListener());


        List<Article> articleList = new ArrayList<Article>();
        Article article = new Article(1,"标题一","shangxin",
                "http://tse2.mm.bing.net/th?id=OIP.Mba385ab1480303bed635743214e0a07bo2&pid=15.1",
                10,20,10,"来自-----","www.baidu.com");
        Article article2 = new Article(1,"标题一","shangxin",
                "http://tse2.mm.bing.net/th?id=OIP.Mba385ab1480303bed635743214e0a07bo2&pid=15.1",
                10,20,10,"来自-----","www.baidu.com");
        articleList.add(article);
        articleList.add(article2);
        ArticleAdapter articleAdapter = new ArticleAdapter(getContext(),articleList,R.layout.item_news);
        ll_content.setAdapter(articleAdapter);
        ll_content.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onDownPullRefresh() {
                Log.d("下拉刷新","====>");

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(3000);
                            handler.sendEmptyMessage(NEWS_REFRESH);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }

            @Override
            public void onLoadingMore() {
                Log.d("上拉加载","====>");
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(3000);
                            handler.sendEmptyMessage(NEWS_LOADMORE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        return view;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startSlider();
    }

    public class SliderPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 轮播线程类
     */
    private class SliderTask implements Runnable{
        @Override
        public void run() {
            synchronized (vp_slider){
                currentItem = (currentItem+1)%imageViewList.size();
                handler.sendEmptyMessage(SLIDER_MEG);
            }
        }
    }

    /**
     * 开始轮播
     */
    public void startSlider(){
        if(scheduledExecutorService==null){
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(new SliderTask(),1,2,TimeUnit.SECONDS);
        }
    }
    /**
     * 停止轮播
     */
    public void stopSlider()  {
        if(scheduledExecutorService!=null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }
}
