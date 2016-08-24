package com.vector.studynews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.vector.studynews.R;
import com.vector.studynews.adapter.ChatAdapter;
import com.vector.studynews.utils.CommonUtils;
import com.vector.studynews.view.chatrow.CustomChatRowProvider;

/**
 * Created by zhang on 2016/8/22.
 */
public class ChatMessageList extends RelativeLayout{
    private static  final String TAG = "chatMessagelist";
    protected Context context;
    protected ListView list;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected EMConversation emConversation;
    protected int chatType;
    protected String toChatUsername;
    protected ChatAdapter messageAdapter;

    protected boolean showUserNick;
    protected boolean showAvatar;
    protected Drawable myBubbleBg;
    protected Drawable otherBuddleBg;

    public ChatMessageList(Context context) {
        this(context,null);
    }


    public ChatMessageList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ChatMessageList(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.chat_message_list,this);
        list = (ListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
    }

    public void init(String toChatUsername,int chatType,CustomChatRowProvider customChatRowProvider){
        this.toChatUsername = toChatUsername;
        this.chatType = chatType;
        emConversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, CommonUtils.getConversationType(chatType),true);
        messageAdapter = new ChatAdapter(context,toChatUsername,chatType,list);
        messageAdapter.setMyBubbleBg(myBubbleBg);
        messageAdapter.setOtherBuddleBg(otherBuddleBg);
        messageAdapter.setShowAvatar(showAvatar);
        messageAdapter.setShowUserNick(showUserNick);
        messageAdapter.setCustomChatRowProvider(customChatRowProvider);
        list.setAdapter(messageAdapter);
    }


    protected void parseStyle(Context context,AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.ChatMessageList);
        showAvatar = ta.getBoolean(R.styleable.ChatMessageList_msgListShowUserAvatar,true);
        myBubbleBg = ta.getDrawable(R.styleable.ChatMessageList_msgListOtherBubbleBackground);
        otherBuddleBg = ta.getDrawable(R.styleable.ChatMessageList_msgListOtherBubbleBackground);
        showUserNick = ta.getBoolean(R.styleable.ChatMessageList_msgListShowUserNick,false);
        ta.recycle();
    }

    /**
     * refresh
     */
    public void refresh(){
        if(messageAdapter!=null){
            messageAdapter.refresh();
        }
    }
    /**
     * refresh and jump to the last
     */
    public void refreshSelectLast(){
        if (messageAdapter != null) {
            messageAdapter.refreshSelectLast();
        }
    }
    /**
     * refresh and jump to the position
     * @param position
     */
    public void refreshSeekTo(int position){
        if (messageAdapter != null) {
            messageAdapter.refreshSeekTo(position);
        }
    }

    public ListView getListView(){return list;}

    public SwipeRefreshLayout getSwipeRefreshLayout(){
        return swipeRefreshLayout;
    }

    public EMMessage getItem(int position){
        return (EMMessage) messageAdapter.getItem(position);
    }

    public void setShowUserNick(boolean showUserNick){
        this.showUserNick = showUserNick;
    }

    public boolean isShowUserNick(){
        return showUserNick;
    }

    /**
     * set click listener
     * @param listener
     */
    public void setItemClickListener(MessageListItemClickListener listener){
        if (messageAdapter != null) {
            messageAdapter.setItemClickListener(listener);
        }
    }

    public interface MessageListItemClickListener{
        void onResendClick(EMMessage message);
        /**
         * there is default handling when bubble is clicked, if you want handle it, return true
         * another way is you implement in onBubbleClick() of chat row
         * @param message
         * @return
         */
        boolean onBubbleClick(EMMessage message);
        void onBubbleLongClick(EMMessage message);
        void onUserAvatarClick(String username);
        void onUserAvatarLongClick(String username);
    }


    /**
     * set chat row provider
     * @param rowProvider
     */
    public void setCustomChatRowProvider(CustomChatRowProvider rowProvider){
        if (messageAdapter != null) {
            messageAdapter.setCustomChatRowProvider(rowProvider);
        }
    }

}
