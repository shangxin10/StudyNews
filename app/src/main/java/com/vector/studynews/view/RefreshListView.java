package com.vector.studynews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vector.studynews.R;
import com.vector.studynews.listener.OnRefreshListener;

/**
 * Created by zhang on 2016/8/17.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener{



    private static final String TAG = "RefreshListView";
    private int firstVisibleItemPosition; // 屏幕显示在第一个的item的索引
    private int headViewHeight = 0 ; //头部的高度
    private View headView;
    private int downY = 0; //下拉的高度
    private ImageView head_arrow;  //下拉箭头
    private ProgressBar head_progress; //加载圈
    private TextView head_tv;

    private final int DOWN_PULL_REFRESH = 0; // 下拉刷新状态
    private final int RELEASE_REFRESH = 1; // 松开刷新
    private final int REFRESHING = 2; // 正在刷新中
    private int currentState = DOWN_PULL_REFRESH; // 头布局的状态: 默认为下拉刷新状态

    private Animation upAnimation; // 向上旋转的动画
    private Animation downAnimation; // 向下旋转的动画

    private int footViewHeight = 0 ; //底部的高度
    private View footView;
    private boolean isScrollToBottom; // 是否滑动到底部
    private ProgressBar foot_progress;
    private boolean isLoadMore = false;
    private OnRefreshListener mOnRefershListener;



    public RefreshListView(Context context, AttributeSet attrs){
        super(context,attrs);
        initHeaderView();
        initFooterView();
        this.setOnScrollListener(this);
    }

    public void initFooterView(){
        footView = View.inflate(getContext(),R.layout.listview_footer,null);
        footView.measure(0,0);
        footViewHeight = footView.getMeasuredHeight();
        Log.d("footHeight",String.valueOf(footViewHeight));
        foot_progress = (ProgressBar) footView.findViewById(R.id.foot_progress);
        footView.setPadding(0,-footViewHeight,0,0);
        this.addFooterView(footView);
    }
    public void initHeaderView(){
        headView = View.inflate(getContext(), R.layout.listview_header,null);
        headView.measure(0,0);
        headViewHeight = headView.getMeasuredHeight();
        Log.d("HeaderHeight",String.valueOf(headViewHeight));
        head_arrow = (ImageView) headView.findViewById(R.id.header_arrow);
        head_progress =(ProgressBar)headView.findViewById(R.id.header_progressbar);
        head_tv = (TextView)headView.findViewById(R.id.header_tv);
        headView.setPadding(0,-headViewHeight,0,0);
        this.addHeaderView(headView);
        initAnimation();
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                // 移动中的y - 按下的y = 间距.
                int diff = (moveY - downY) / 2;
                // -头布局的高度 + 间距 = paddingTop
                int paddingTop = -headViewHeight + diff;
                // 如果: -头布局的高度 > paddingTop的值 执行super.onTouchEvent(ev);
                if (firstVisibleItemPosition == 0 && -headViewHeight < paddingTop) {
                    if (paddingTop > 0 && currentState == DOWN_PULL_REFRESH) { // 完全显示了.
                        Log.i(TAG, "松开刷新");
                        currentState = RELEASE_REFRESH;
                        RefreshHeader();
                    } else if (paddingTop < 0 && currentState == RELEASE_REFRESH) { // 没有显示完全
                        Log.i(TAG, "下拉刷新");
                        currentState = DOWN_PULL_REFRESH;
                        RefreshHeader();
                    }
                    // 下拉头布局
                    headView.setPadding(0, paddingTop, 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                // 判断当前的状态是松开刷新还是下拉刷新
                if (currentState == RELEASE_REFRESH) {
                    Log.i(TAG, "刷新数据.");
                    // 把头布局设置为完全显示状态
                    headView.setPadding(0, 0, 0, 0);
                    // 进入到正在刷新中状态
                    currentState = REFRESHING;
                    RefreshHeader();
                    if (mOnRefershListener != null) {
                        mOnRefershListener.onDownPullRefresh(); // 调用使用者的监听方法
                    }
                } else if (currentState == DOWN_PULL_REFRESH) {
                    // 隐藏头布局
                    headView.setPadding(0, -headViewHeight, 0, 0);
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
    /**
     * 当滚动状态改变时回调
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            // 判断当前是否已经到了底部
            if (isScrollToBottom && !isLoadMore) {
                isLoadMore = true;
                // 当前到底部
                footView.setPadding(0, 0, 0, 0);
                this.setSelection(this.getCount());

                if (mOnRefershListener != null) {
                    mOnRefershListener.onLoadingMore();
                }
            }
        }
    }
    /**
     * 当滚动时调用
     *
     * @param firstVisibleItem
     *            当前屏幕显示在顶部的item的position
     * @param visibleItemCount
     *            当前屏幕显示了多少个条目的总数
     * @param totalItemCount
     *            ListView的总条目的总数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        firstVisibleItemPosition = firstVisibleItem;
        if (getLastVisiblePosition() == (totalItemCount - 1)) {
            isScrollToBottom = true;
        } else {
            isScrollToBottom = false;
        }
    }

    //头部初始化动画
    public void initAnimation(){
        upAnimation = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上

        downAnimation = new RotateAnimation(-180f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上
    }

    //隐藏头部
    public void hideHeadView(){
        headView.setPadding(0,-headViewHeight,0,0);
        head_arrow.setVisibility(VISIBLE);
        head_progress.setVisibility(GONE);
        currentState = DOWN_PULL_REFRESH;

    }
    //隐藏底部
    public void hideFooterView(){
        footView.setPadding(0,-footViewHeight,0,0);
        isLoadMore = false;
    }

    //刷新头部状态
    public void RefreshHeader(){
        switch (currentState){
            case DOWN_PULL_REFRESH:
                head_tv.setText("下拉刷新");
                head_arrow.startAnimation(downAnimation);
                break;
            case RELEASE_REFRESH:
                head_tv.setText("松开加载");
                head_arrow.startAnimation(upAnimation);
                break;
            case REFRESHING:
                head_arrow.clearAnimation();
                head_arrow.setVisibility(INVISIBLE);
                head_progress.setVisibility(VISIBLE);
                head_tv.setText("正在刷新");
                break;
            default:
                break;
        }
    }

    /**
     * 设置刷新监听事件
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefershListener = listener;
    }
}
