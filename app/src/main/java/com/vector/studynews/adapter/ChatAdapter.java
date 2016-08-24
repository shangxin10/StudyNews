package com.vector.studynews.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.vector.studynews.utils.CommonUtils;
import com.vector.studynews.view.ChatMessageList;
import com.vector.studynews.view.chatrow.CustomChatRowProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/8/22.
 */
public class ChatAdapter extends BaseAdapter {

    private static final String TAG ="chatAdapter";

    private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
    private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
    private static final int HANDLER_MESSAGE_SEEK_TO = 2;

    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
    private static final int MESSAGE_TYPE_SENT_VOICE = 6;
    private static final int MESSAGE_TYPE_RECV_VOICE = 7;
    private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
    private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
    private static final int MESSAGE_TYPE_SENT_FILE = 10;
    private static final int MESSAGE_TYPE_RECV_FILE = 11;
    private static final int MESSAGE_TYPE_SENT_EXPRESSION = 12;
    private static final int MESSAGE_TYPE_RECV_EXPRESSION = 13;

    private Context context;
    private String toChatUsername;
    private int chatType;
    private List<EMMessage> messageList = new ArrayList<EMMessage>();
    private EMConversation emConversation;
    private ListView listView;
    private ChatMessageList.MessageListItemClickListener itemClickListener;
    private CustomChatRowProvider customChatRowProvider;
    private boolean showUserNick;
    private boolean showAvatar;
    private Drawable myBubbleBg;
    private Drawable otherBuddleBg;

    Handler handler = new Handler(){
        private void refreshList(){
            messageList = emConversation.getAllMessages();
            emConversation.markAllMessagesAsRead();
            notifyDataSetChanged();
        }

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case HANDLER_MESSAGE_REFRESH_LIST:
                    refreshList();
                    break;
                case HANDLER_MESSAGE_SEEK_TO:
                    int position = msg.arg1;
                    listView.setSelection(position);
                    break;
                case HANDLER_MESSAGE_SELECT_LAST:
                    if (messageList.size() > 0) {
                        listView.setSelection(messageList.size()- 1);
                    }
                    break;
                default:break;
            }
        }
    };

    public ChatAdapter(Context context,String toChatUsername,int chatType,ListView listView){
        this.context = context;
        this.chatType = chatType;
        this.listView = listView;
        this.toChatUsername = toChatUsername;
        emConversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, CommonUtils.getConversationType(chatType),true);
    }

    @Override
    public int getCount() {
        return messageList==null?0:messageList.size();
    }

    @Override
    public Object getItem(int position) {
        if(messageList!=null&&position<messageList.size()){
            return messageList.get(position);
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch(getItemViewType(position)){
            case MESSAGE_TYPE_RECV_TXT:

        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = messageList.get(position);
        if(emMessage.getType()== EMMessage.Type.TXT){
            return emMessage.direct()== EMMessage.Direct.RECEIVE?MESSAGE_TYPE_RECV_TXT:MESSAGE_TYPE_SENT_TXT;
        }
        return -1;
    }

    public void refresh() {
        if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
            return;
        }
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
        handler.sendMessage(msg);
    }

    /**
     * refresh and select the last
     */
    public void refreshSelectLast() {
        final int TIME_DELAY_REFRESH_SELECT_LAST = 100;
        handler.removeMessages(HANDLER_MESSAGE_REFRESH_LIST);
        handler.removeMessages(HANDLER_MESSAGE_SELECT_LAST);
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_REFRESH_LIST, TIME_DELAY_REFRESH_SELECT_LAST);
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_SELECT_LAST, TIME_DELAY_REFRESH_SELECT_LAST);
    }
    /**
     * refresh and seek to the position
     */
    public void refreshSeekTo(int position) {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
        msg.arg1 = position;
        handler.sendMessage(msg);
    }

    public void setItemClickListener(ChatMessageList.MessageListItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public void setShowUserNick(boolean showUserNick) {
        this.showUserNick = showUserNick;
    }


    public void setShowAvatar(boolean showAvatar) {
        this.showAvatar = showAvatar;
    }


    public void setMyBubbleBg(Drawable myBubbleBg) {
        this.myBubbleBg = myBubbleBg;
    }


    public void setOtherBuddleBg(Drawable otherBuddleBg) {
        this.otherBuddleBg = otherBuddleBg;
    }


    public void setCustomChatRowProvider(CustomChatRowProvider customChatRowProvider){
        this.customChatRowProvider = customChatRowProvider;
    }
    public boolean isShowUserNick() {
        return showUserNick;
    }


    public boolean isShowAvatar() {
        return showAvatar;
    }


    public Drawable getMyBubbleBg() {
        return myBubbleBg;
    }


    public Drawable getOtherBuddleBg() {
        return otherBuddleBg;
    }
}
