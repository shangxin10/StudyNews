package com.vector.studynews.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.vector.studynews.R;
import com.vector.studynews.entity.Emojicon;
import com.vector.studynews.entity.EmojiconGroupEntity;
import com.vector.studynews.model.DefaultEmojiconDatas;
import com.vector.studynews.utils.SmileUtils;
import com.vector.studynews.view.emojicon.EmojiconMenu;
import com.vector.studynews.view.emojicon.EmojiconMenuBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhang on 2016/8/20.
 */
public class ChatInputMenu extends LinearLayout {
    private static final String TAG ="ChatInputMenu";
    FrameLayout primaryMenuContainer,emojiconMenuContainer;
    private LayoutInflater layoutInflater;
    private Context context;
    protected FrameLayout chatExtendMenuContainer;
    protected ChatPrimaryMenuBase chatPrimaryMenu;
    protected EmojiconMenuBase emojiconMenu;
    protected ChatExtendMenu chatExtendMenu;

    private ChatInputMenuListener listener;
    private Handler handler = new Handler();
    private boolean inited;
    public ChatInputMenu(Context context) {
        super(context);
        init(context,null);
    }

    public ChatInputMenu(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context,attributeSet);
    }
    public ChatInputMenu(Context context,AttributeSet attributeSet,int defstyle){
        this(context,attributeSet);

    }
    public void init(Context context,AttributeSet attributeSet){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.widget_chat_input_menu,this);
        primaryMenuContainer = (FrameLayout) findViewById(R.id.primary_menu_container);
        emojiconMenuContainer = (FrameLayout) findViewById(R.id.emojicon_menu_container);
        chatExtendMenuContainer = (FrameLayout)findViewById(R.id.extend_menu_container);
        chatExtendMenu = (ChatExtendMenu)findViewById(R.id.extend_menu);
    }
    public void init(List<EmojiconGroupEntity> emojiconGroupEntityList){
        if(inited){
            return;
        }
        if(chatPrimaryMenu==null){
            chatPrimaryMenu = (ChatPrimaryMenu) layoutInflater.inflate(R.layout.layout_chat_primary_menu,null);
        }
        primaryMenuContainer.addView(chatPrimaryMenu);
        if(emojiconMenu==null){

            emojiconMenu = (EmojiconMenu) layoutInflater.inflate(R.layout.layout_emojicon_menu,null);
            if(emojiconGroupEntityList ==null){
                emojiconGroupEntityList = new ArrayList<EmojiconGroupEntity>();
                emojiconGroupEntityList.add(new EmojiconGroupEntity(R.mipmap.ee_1, Arrays.asList(DefaultEmojiconDatas.getData())));

            }

            ((EmojiconMenu)emojiconMenu).init(emojiconGroupEntityList);
        }
        emojiconMenuContainer.addView(emojiconMenu);
        processChatMenu();
        chatExtendMenu.init();
        inited = true;
    }
    public void init(){
        init(null);
    }
    /**
     * set custom emojicon menu
     * @param customEmojiconMenu
     */
    public void setCustomEmojiconMenu(EmojiconMenuBase customEmojiconMenu){
        this.emojiconMenu = customEmojiconMenu;
    }

    /**
     * set custom primary menu
     * @param customPrimaryMenu
     */
    public void setCustomPrimaryMenu(ChatPrimaryMenuBase customPrimaryMenu){
        this.chatPrimaryMenu = customPrimaryMenu;
    }

    public ChatPrimaryMenuBase getPrimaryMenu(){
        return chatPrimaryMenu;
    }

    public ChatExtendMenu getExtendMenu(){
        return chatExtendMenu;
    }

    public EmojiconMenuBase getEmojiconMenu(){
        return emojiconMenu;
    }

    /**
     * register menu item
     *
     * @param name
     *            item name
     * @param drawableRes
     *            background of item
     * @param itemId
     *             id
     * @param listener
     *            on click event of item
     */
    public void registerExtendMenuItem(String name, int drawableRes, int itemId,
                                       ChatExtendMenu.ChatExtendMenuItemClickListener listener) {
        chatExtendMenu.registerMenuItem(name, drawableRes, itemId, listener);
    }

    /**
     * register menu item
     *
     * @param nameRes
     *            resource id of item name
     * @param drawableRes
     *            background of item
     * @param itemId
     *             id
     * @param listener
     *            on click event of item
     */
    public void registerExtendMenuItem(int nameRes, int drawableRes, int itemId,
                                       ChatExtendMenu.ChatExtendMenuItemClickListener listener) {
        chatExtendMenu.registerMenuItem(nameRes, drawableRes, itemId, listener);
    }


    protected void processChatMenu(){
        chatPrimaryMenu.setChatPrimaryMenuListener(new ChatPrimaryMenuBase.ChatPrimaryMenuListener() {
            @Override
            public void onSendBtnClicked(String content) {
                if(listener!=null){
                    listener.onSendMessage(content);
                }
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent motionEvent) {
                if(listener !=null){
                    return listener.onPressToSpeakBtnTouch(v,motionEvent);
                }
                return false;
            }

            @Override
            public void onToggleVoiceBtnClicked() {
                hideExtendMenuContainer();
            }

            @Override
            public void onToggleExtendClicked() {
                toggleMore();
            }

            @Override
            public void onToggleEmojiconClicked() {
                toggleEmojicon();
            }

            @Override
            public void onEditTextClicked() {
                hideExtendMenuContainer();
            }
        });
        emojiconMenu.setEmojiconMenuListener(new EmojiconMenuBase.EmojiconMenuListener() {
            @Override
            public void onExpressionClicked(Emojicon emojicon) {
                if(emojicon.getType()!=Emojicon.Type.BIG_EXPRESSION){
                    if(emojicon.getEmojiText()!=null){
                        chatPrimaryMenu.onEmojiconInputEvent(SmileUtils.getSmiledText(context,emojicon.getEmojiText()));
                    }
                }else{
                    if(listener!=null){
                        listener.onBigExpressionClicked(emojicon);
                    }
                }
            }

            @Override
            public void onDeleteImageClicked() {
                chatPrimaryMenu.onEmojiconDeleteEvent();
            }
        });
    }


    public void insertText(String text){
        getPrimaryMenu().onTextInsert(text);
    }

    private void hideKeyboard(){
        chatPrimaryMenu.hideKeyboard();
    }
    public void hideExtendMenuContainer(){
        chatExtendMenu.setVisibility(GONE);
        emojiconMenu.setVisibility(GONE);
        chatExtendMenuContainer.setVisibility(GONE);
        chatPrimaryMenu.onExtendMenuContainerHide();
    }


    protected  void toggleMore(){
        if(chatExtendMenuContainer.getVisibility() == GONE){
            hideKeyboard();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatExtendMenuContainer.setVisibility(VISIBLE);
                    chatExtendMenu.setVisibility(VISIBLE);
                    emojiconMenu.setVisibility(GONE);
                }
            },50);
        }else{
            if(emojiconMenu.getVisibility()==View.VISIBLE){
                emojiconMenu.setVisibility(GONE);
                chatExtendMenu.setVisibility(VISIBLE);
            }else{
                chatExtendMenuContainer.setVisibility(GONE);
            }
        }
    }
    protected  void toggleEmojicon(){
        if(chatExtendMenuContainer.getVisibility()==GONE){
            hideKeyboard();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatExtendMenuContainer.setVisibility(VISIBLE);
                    emojiconMenu.setVisibility(VISIBLE);
                    chatExtendMenu.setVisibility(GONE);
                }
            },50);
        }else{
            if(emojiconMenu.getVisibility()==VISIBLE){
                chatExtendMenuContainer.setVisibility(GONE);
                emojiconMenu.setVisibility(GONE);
            }else{
                chatExtendMenu.setVisibility(GONE);
                emojiconMenu.setVisibility(VISIBLE);
            }
        }
    }

    public boolean onBackPressed(){
        if(chatExtendMenuContainer.getVisibility()==VISIBLE){
            hideExtendMenuContainer();
            return false;
        }else{
            return true;
        }
    }

    public void setChatInputMenuListener(ChatInputMenuListener listener){
        this.listener = listener;
    }

    public interface ChatInputMenuListener {
        /**
         * when send message button pressed
         *
         * @param content
         *            message content
         */
        void onSendMessage(String content);

        /**
         * when big icon pressed
         * @param emojicon
         */
        void onBigExpressionClicked(Emojicon emojicon);

        /**
         * when speak button is touched
         * @param v
         * @param event
         * @return
         */
        boolean onPressToSpeakBtnTouch(View v, MotionEvent event);
    }

}
