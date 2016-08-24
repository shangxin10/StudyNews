package com.vector.studynews.view.chatrow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.vector.studynews.R;
import com.vector.studynews.adapter.ChatAdapter;
import com.vector.studynews.utils.DateUtils;
import com.vector.studynews.utils.UserUtils;
import com.vector.studynews.view.ChatMessageList;

import java.util.Date;

/**
 * Created by zhang on 2016/8/22.
 */
public abstract class ChatRow extends LinearLayout{
    protected Activity activity;
    protected LayoutInflater inflater;
    protected Context context;
    protected EMMessage message;
    protected int position;
    protected BaseAdapter adapter;

    protected ImageView userAvatarView;
    protected TextView timestamp;
    protected View bubbleLayout;
    protected TextView usernickView;

    protected ProgressBar progressBar;
    protected ImageView statusView;
    protected TextView percentageView;

    protected EMCallBack messageSendCallback;
    protected EMCallBack messageReceiveCallback;


    protected ChatMessageList.MessageListItemClickListener itemClickListener;

    public ChatRow(Context context, EMMessage message, int positioin, BaseAdapter adapter){
        super(context);
        this.adapter = adapter;
        this.position = positioin;
        this.context = context;
        this.activity = (Activity) context;
        this.message = message;
        inflater = LayoutInflater.from(context);
        initView();
    }

    public void initView(){
        onInfaterView();
        userAvatarView = (ImageView) findViewById(R.id.iv_userhead);
        timestamp = (TextView) findViewById(R.id.timestamp);
        bubbleLayout = findViewById(R.id.bubble);
        usernickView = (TextView) findViewById(R.id.tv_userid);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        statusView = (ImageView) findViewById(R.id.msg_status);
        onFindViewById();
    }

    public void setUpView(EMMessage message, int position,
                          ChatMessageList.MessageListItemClickListener itemClickListener){
        this.message = message;
        this.position = position;
        this.itemClickListener = itemClickListener;

        setUpbaseView();
        onSetUpView();

    }
    private void setUpbaseView(){
        // set nickname, avatar and background of bubble
        TextView tiemstamp = (TextView) findViewById(R.id.timestamp);
        if(tiemstamp!=null){
            if(position==0){
                tiemstamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                tiemstamp.setVisibility(VISIBLE);
            }else{
                EMMessage prevMessage = (EMMessage) adapter.getItem(position-1);
                if(prevMessage!=null&&DateUtils.isCloseEnough(prevMessage.getMsgTime(),message.getMsgTime())){
                    tiemstamp.setVisibility(GONE);
                }else{
                    tiemstamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    tiemstamp.setVisibility(VISIBLE);
                }
            }
        }
        //set avatar and nickname
        if(message.direct() == EMMessage.Direct.SEND){
            UserUtils.setUserAvatar(context, EMClient.getInstance().getCurrentUser(),userAvatarView);
        }else{
            UserUtils.setUserNick(message.getFrom(),usernickView);
            UserUtils.setUserAvatar(context,message.getFrom(),userAvatarView);
        }

        //
        if (adapter instanceof ChatAdapter) {
            if (((ChatAdapter) adapter).isShowAvatar())
                userAvatarView.setVisibility(View.VISIBLE);
            else
                userAvatarView.setVisibility(View.GONE);
            if (usernickView != null) {
                if (((ChatAdapter) adapter).isShowUserNick())
                    usernickView.setVisibility(View.VISIBLE);
                else
                    usernickView.setVisibility(View.GONE);
            }
            if (message.direct() == EMMessage.Direct.SEND) {
                if (((ChatAdapter) adapter).getMyBubbleBg() != null) {
                    bubbleLayout.setBackgroundDrawable(((ChatAdapter) adapter).getMyBubbleBg());
                }
            } else if (message.direct() == EMMessage.Direct.RECEIVE) {
                if (((ChatAdapter) adapter).getOtherBuddleBg() != null) {
                    bubbleLayout.setBackgroundDrawable(((ChatAdapter) adapter).getOtherBuddleBg());
                }
            }
        }
    }

    protected void setMessageSendCallBack(){
        if(messageSendCallback==null){
            messageSendCallback = new EMCallBack() {
                @Override
                public void onSuccess() {
                    updateView();
                }

                @Override
                public void onError(int i, String s) {
                    updateView();
                }

                @Override
                public void onProgress(final int i, String s) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            percentageView.setText(i+"%");
                        }
                    });
                }
            };
        }
        message.setMessageStatusCallback(messageSendCallback);
    }

    protected void setMessageReceiveCallback(){
        if(messageReceiveCallback==null){
            messageSendCallback = new EMCallBack() {
                @Override
                public void onSuccess() {
                    updateView();
                }

                @Override
                public void onError(int i, String s) {
                    updateView();
                }

                @Override
                public void onProgress(final int i, String s) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(percentageView!=null){
                                percentageView.setText(i+"%");
                            }
                        }
                    });

                }
            };
        }
        message.setMessageStatusCallback(messageReceiveCallback);
    }


    private void setClickListener(){
        if(bubbleLayout!=null){
            bubbleLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener!=null){
                        if(!itemClickListener.onBubbleClick(message)){
                            onBubbleClick();
                        }
                    }
                }
            });
            bubbleLayout.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(itemClickListener!=null){
                        itemClickListener.onBubbleLongClick(message);
                    }
                    return true;
                }
            });
        }

        if(statusView!=null){
            statusView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener!=null){

                        itemClickListener.onResendClick(message);
                    }
                }
            });
        }
        if(userAvatarView!=null){
            userAvatarView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener!=null){
                        if(message.direct()== EMMessage.Direct.SEND){
                            itemClickListener.onUserAvatarClick(EMClient.getInstance().getCurrentUser());
                        }else{
                            itemClickListener.onUserAvatarClick(message.getFrom());
                        }
                    }
                }
            });
            userAvatarView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(itemClickListener!=null){

                        if(message.direct() == EMMessage.Direct.SEND){
                            itemClickListener.onUserAvatarLongClick(EMClient.getInstance().getCurrentUser());
                        }else{
                            itemClickListener.onUserAvatarLongClick(message.getFrom());
                        }

                        return true;
                    }
                    return false;
                }
            });
        }
    }


    protected void updateView(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(message.status()==EMMessage.Status.FAIL){
                    if (message.getError() == EMError.MESSAGE_INCLUDE_ILLEGAL_CONTENT) {
                        Toast.makeText(activity,activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_invalid_content), Toast.LENGTH_LONG).show();
                    } else if (message.getError() == EMError.GROUP_NOT_JOINED) {
                        Toast.makeText(activity,activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_not_in_the_group), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(activity,activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        onUpdateView();
    }
    protected abstract void onInfaterView();
    /**
     * find view by id
     */
    protected abstract void onFindViewById();
    /**
     * refresh list view when message status change
     */
    protected abstract void onUpdateView();

    /**
     * setup view
     *
     */
    protected abstract void onSetUpView();

    /**
     * on bubble clicked
     */
    protected abstract void onBubbleClick();


}
