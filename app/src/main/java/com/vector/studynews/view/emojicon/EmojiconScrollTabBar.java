package com.vector.studynews.view.emojicon;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hyphenate.util.DensityUtil;
import com.vector.studynews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/8/21.
 */
public class EmojiconScrollTabBar extends RelativeLayout {
    private Context context;
    private HorizontalScrollView scrollView;
    private LinearLayout tabContainer;
    private List<ImageView> tabList = new ArrayList<ImageView>();
    private ScrollTabBarItemClickListener itemClickListener;
    private int tabWidth = 60;
    public EmojiconScrollTabBar(Context context) {
        this(context,null);
    }

    public EmojiconScrollTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public EmojiconScrollTabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }
    public void init(Context context,AttributeSet attributeSet){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.widget_emojicon_tab_bar,this);
        scrollView = (HorizontalScrollView) findViewById(R.id.scroll_view);
        tabContainer = (LinearLayout) findViewById(R.id.tab_container);
    }

    public void addTab(int icon){
        View tabView = View.inflate(context,R.layout.scroll_tab_item,null);
        ImageView imageView = (ImageView)tabView.findViewById(R.id.iv_icon);
        imageView.setImageResource(icon);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(context,tabWidth), LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imgParams);
        tabContainer.addView(tabView);
        tabList.add(imageView);
        final int position = tabList.size()-1;
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener!=null){
                    itemClickListener.onItemClick(position);
                }
            }
        });
    }
    public void removeTab(int position){
        tabContainer.removeViewAt(position);
        tabList.remove(position);
    }
    public void selectedTo(int position){
        scrollTo(position);
        for(int i = 0;i<tabList.size();i++){
            if(position==i){
                tabList.get(i).setBackgroundColor(getResources().getColor(R.color.emojicon_tab_selected));
            }else{
                tabList.get(i).setBackgroundColor(getResources().getColor(R.color.emojicon_tab_nomal));
            }
        }
    }
    private void scrollTo(final int position){
        int childCount = tabContainer.getChildCount();
        if(position<childCount){
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    int mScrollX = tabContainer.getScrollX();
                    int childX = (int) ViewCompat.getX(tabContainer.getChildAt(position));
                    if(childX <mScrollX){
                        scrollView.scrollTo(childX,0);
                        return;
                    }
                    int childwidth = (int)tabContainer.getChildAt(position).getWidth();
                    int hsvwidth = scrollView.getWidth();
                    int childRight = childX + childwidth;
                    int scrollRight = mScrollX + hsvwidth;
                    if(childRight > scrollRight){
                        scrollView.scrollTo(childRight-scrollRight,0);
                        return;
                    }
                }
            });
        }
    }
    public void setTabBarItemClickListener(ScrollTabBarItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface ScrollTabBarItemClickListener{
        void onItemClick(int position);
    }
}
