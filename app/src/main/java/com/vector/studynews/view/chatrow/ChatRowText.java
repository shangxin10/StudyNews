package com.vector.studynews.view.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.vector.studynews.R;
import com.vector.studynews.utils.SmileUtils;

/**
 * Created by zhang on 2016/8/23.
 */
public class ChatRowText extends ChatRow {
    private TextView textView;


    public ChatRowText(Context context, EMMessage message, int positioin, BaseAdapter adapter) {
        super(context, message, positioin, adapter);
    }

    @Override
    protected void onInfaterView() {
        inflater.inflate(message.direct()== EMMessage.Direct.SEND?
                R.layout.row_send_message:R.layout.row_received_message,this);
    }

    @Override
    protected void onFindViewById() {
        textView = (TextView) findViewById(R.id.tv_chatcontent);
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSetUpView() {
        EMTextMessageBody texBody = (EMTextMessageBody) message.getBody();
        Spannable span = SmileUtils.getSmiledText(context, texBody.getMessage());
        //设置内容
        textView.setText(span, TextView.BufferType.SPANNABLE);
        handleTextMessage();
    }

    @Override
    protected void onBubbleClick() {

    }


    protected void handleTextMessage(){
        if(message.direct()== EMMessage.Direct.SEND){
            setMessageSendCallBack();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }

    }
}
