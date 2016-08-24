package com.vector.studynews.ui.activity;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.vector.studynews.R;
import com.vector.studynews.adapter.MyFragmentPageAdapter;
import com.vector.studynews.ui.fragment.DiscoverFragment;
import com.vector.studynews.ui.fragment.IndexFragment;
import com.vector.studynews.ui.fragment.MessageFragment;
import com.vector.studynews.ui.fragment.MineFragment;
import com.vector.studynews.ui.fragment.SubscribeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG="MainActivity";
    public static final int INDEX_POS = 0;
    public static final int SUBSCRIBE_POS = 1;
    public static final int MESSAGE_POS =2;
    public static final int DISCOVER_POS = 3;
    public static final int MSG_INDEX = 4;
    public static final int MSG_SUBSCRIBE = 5;
    public static final int MSG_MESSAGE = 6;
    public static final int MSG_DISCOVER = 7;
    public static final int DISMISS_INDEX = 8;
    public static final int DISMISS_SUBSCRIBE = 9;
    public static final int DISMISS_MESSAGE = 10;
    public static final int DISMISS_DISCOVER = 11;
    private ImageView menu_iv_index,menu_iv_discover,menu_iv_message,menu_iv_subscribe;
    private RelativeLayout rl_index,rl_discover,rl_message,rl_subscribe,rl_mine;
    private TextView focus_index,focus_subscribe,focus_message,focus_discover;
    private TextView tv_chat,tv_search;
    private ViewPager viewPager;
    private Fragment indexFragment,messageFragment,subscribeFragment,discoverFragment;
    private MineFragment mineFragment;
    private MyFragmentPageAdapter myFragmentPageAdapter;
    private List<Fragment> fragmentList;
    private DrawerLayout drawerLayout;
    private Context context;
    private List<TextView> menu_list =  new ArrayList<TextView>();
    private IndexFragment IndexFragment;
    private int currentFragment ;    //当前fragment的序号

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_MESSAGE:
                    menu_iv_message.setVisibility(View.VISIBLE);
                    break;
                case MSG_INDEX:
                    menu_iv_index.setVisibility(View.VISIBLE);
                    break;
                case MSG_DISCOVER:
                    menu_iv_discover.setVisibility(View.VISIBLE);
                    break;
                case MSG_SUBSCRIBE:
                    menu_iv_subscribe.setVisibility(View.VISIBLE);
                    break;
                case DISCOVER_POS:
                    menu_iv_discover.setVisibility(View.INVISIBLE);
                    break;
                case INDEX_POS:
                    menu_iv_index.setVisibility(View.INVISIBLE);
                    break;
                case MESSAGE_POS:
                    menu_iv_message.setVisibility(View.INVISIBLE);
                    break;
                case SUBSCRIBE_POS:
                    menu_iv_subscribe.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        //初始化导航栏文字
        rl_discover = (RelativeLayout) findViewById(R.id.rl_discover);
        rl_index = (RelativeLayout) findViewById(R.id.rl_index);
        rl_message = (RelativeLayout) findViewById(R.id.rl_message);
        rl_subscribe = (RelativeLayout) findViewById(R.id.rl_subscribe);

        //初始化导航栏红点
        menu_iv_index = (ImageView) findViewById(R.id.menu_iv_index);
        menu_iv_subscribe = (ImageView) findViewById(R.id.menu_iv_subscribe);
        menu_iv_message = (ImageView) findViewById(R.id.menu_iv_message);
        menu_iv_discover = (ImageView) findViewById(R.id.menu_iv_discover);


        //初始化搜索图标和聊天图标
        tv_chat = (TextView) findViewById(R.id.tv_chat);
        tv_search = (TextView) findViewById(R.id.tv_search);


        //菜单选中
        focus_index =  (TextView)findViewById(R.id.focus_index);
        focus_discover = (TextView)findViewById(R.id.focus_discover);
        focus_message = (TextView)findViewById(R.id.focus_message);
        focus_subscribe = (TextView)findViewById(R.id.focus_subscribe);

        //添加到一个list中,方便修改
        menu_list.add(focus_index);
        menu_list.add(focus_subscribe);
        menu_list.add(focus_message);
        menu_list.add(focus_discover);


        //初始化fragment
        indexFragment = new IndexFragment();
        messageFragment = new MessageFragment();
        subscribeFragment = new SubscribeFragment();
        discoverFragment = new DiscoverFragment();
        mineFragment = (MineFragment)getSupportFragmentManager().findFragmentById(R.id.fg_mine);
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(indexFragment);
        fragmentList.add(subscribeFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(discoverFragment);

        viewPager = (ViewPager)findViewById(R.id.vp_content);
        myFragmentPageAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(myFragmentPageAdapter);
        viewPager.addOnPageChangeListener(new MyPageChangeListener());

        //初始化侧滑
        rl_mine = (RelativeLayout) findViewById(R.id.rl_mine);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);

        //设置侧滑只能通过程序打开
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,Gravity.START);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener(){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,Gravity.START);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        mineFragment.setDrawerLayout(drawerLayout);
        rl_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(INDEX_POS);
                setMenuFocus(INDEX_POS);
                startSlider();
            }
        });
        rl_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(SUBSCRIBE_POS);
                setMenuFocus(SUBSCRIBE_POS);
                stopSlider();
            }
        });
        rl_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(MESSAGE_POS);
                setMenuFocus(MESSAGE_POS);
                stopSlider();
            }
        });
        rl_discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(DISCOVER_POS);
                setMenuFocus(DISCOVER_POS);
                stopSlider();
            }
        });
        rl_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"click",Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(Gravity.START);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,Gravity.START);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //显示第一fragment
        viewPager.setCurrentItem(INDEX_POS);
        setMenuFocus(INDEX_POS);

        EMClient.getInstance().chatManager().addMessageListener(new MessageListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onresume");

    }

    /**
     * fragment 切换时导航栏的变化
     * @param index
     */
    public void setMenuFocus(int index){
        //设置当前的页面序号
        currentFragment = index;

        //将所有导航栏的底部选中条隐藏
        for(TextView t:menu_list){
            t.setVisibility(View.INVISIBLE);
        }

        //显示选中的导航栏底部
        menu_list.get(index).setVisibility(View.VISIBLE);

        //显示搜索图标还是聊天图标
        if(index==MESSAGE_POS){
            tv_chat.setVisibility(View.VISIBLE);
            tv_search.setVisibility(View.INVISIBLE);
        }else{
            tv_chat.setVisibility(View.INVISIBLE);
            tv_search.setVisibility(View.VISIBLE);
        }

        //去除红圈
        handler.sendEmptyMessage(index);
    }

    /**
     * 页面切换
     */
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setMenuFocus(position);
            switch (position){
                case INDEX_POS:
                                startSlider();
                                break;
                case SUBSCRIBE_POS:
                case DISCOVER_POS:
                case MESSAGE_POS:
                                stopSlider();
                                break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }


    }

    public class MessageListener implements EMMessageListener{

        @Override
        public void onMessageReceived(List<EMMessage> list) {
            Log.d(TAG,"收到消息");
            //不是当前页面
            if(currentFragment!=MESSAGE_POS){
                handler.sendEmptyMessage(MSG_MESSAGE);
            }

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    }
    /**
     * 开始轮播
     */
    public void startSlider(){
        IndexFragment = (IndexFragment)fragmentList.get(INDEX_POS);
        IndexFragment.startSlider();
    }

    /**
     * 暂停轮播
     */
    public void stopSlider(){
        IndexFragment = (IndexFragment) fragmentList.get(INDEX_POS);
        IndexFragment.stopSlider();
    }


}
