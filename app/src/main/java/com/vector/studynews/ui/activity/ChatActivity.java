package com.vector.studynews.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vector.studynews.R;
import com.vector.studynews.config.Config;
import com.vector.studynews.entity.Emojicon;
import com.vector.studynews.view.ChatExtendMenu;
import com.vector.studynews.view.ChatInputMenu;
import com.vector.studynews.view.ChatMessageList;

/**
 * Created by zhang on 2016/8/19.
 */
public class ChatActivity extends AppCompatActivity {
    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_LOCATION = 3;

    private ImageView iv_back;
    private ImageView iv_delete;
    private TextView chat_title;
    private ChatMessageList chatMessageList;
    private ChatInputMenu chatInputMenu;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;

    private int chatType;
    private String toUserName;

    private int[] itemStrings = { R.string.attach_take_pic, R.string.attach_picture, R.string.attach_location };
    private int[] itemdrawables = { R.drawable.chat_takepic_selector, R.drawable.chat_image_selector,
            R.drawable.chat_location_selector };
    private int[] itemIds = { ITEM_TAKE_PICTURE, ITEM_PICTURE, ITEM_LOCATION };

    private MyItemClickListener extendMenuItemClickListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //请求发送过来的i
        Intent i = this.getIntent();
        chatType = i.getStringExtra(Config.EXTRA_CHAR_TYPE)==null?Config.CHATTYPE_SINGLE:
                Integer.valueOf(i.getStringExtra(Config.EXTRA_CHAR_TYPE));
        toUserName = i.getStringExtra(Config.EXTRA_USER_ID);
        initView();
    }

    public void initView(){
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        chat_title = (TextView) findViewById(R.id.chat_title);
        chatMessageList = (ChatMessageList) findViewById(R.id.message_list);
        //设置名字可见
        chatMessageList.setShowUserNick(true);
        listView = chatMessageList.getListView();
        swipeRefreshLayout = chatMessageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        chatInputMenu = (ChatInputMenu) findViewById(R.id.inputmenu);
        registerExtendMenuItem();
        chatInputMenu.init();
        chatInputMenu.setChatInputMenuListener(new ChatInputMenu.ChatInputMenuListener() {
            @Override
            public void onSendMessage(String content) {

            }

            @Override
            public void onBigExpressionClicked(Emojicon emojicon) {

            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return false;
            }
        });

    }


    public void setUpView(){

    }

    /**
     * register extend menu, item id need > 3 if you override this method and keep exist item
     */
    protected void registerExtendMenuItem(){
        for(int i = 0; i < itemStrings.length; i++){
            chatInputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }

    class MyItemClickListener implements ChatExtendMenu.ChatExtendMenuItemClickListener{

        @Override
        public void onClick(int itemId, View view) {
            switch(itemId){
                case ITEM_TAKE_PICTURE:
                    break;
                case ITEM_PICTURE:
                    break;
                case ITEM_LOCATION:
                    break;
                default: break;
            }
        }
    }


}
