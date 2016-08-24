package com.vector.studynews.adapter;

import android.content.Context;
import android.widget.ListView;

import com.hyphenate.chat.EMConversation;
import com.vector.studynews.R;
import com.vector.studynews.utils.DateUtils;
import com.vector.studynews.utils.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhang on 2016/8/18.
 */
public class MessageAdapter extends CommonAdapter<EMConversation>{

    private SimpleDateFormat format =  new SimpleDateFormat("HH:mm");


    public MessageAdapter(Context context, List<EMConversation> datas,int layoutId){
        super(context,datas,layoutId);
    }


    @Override
    public void convert(int position, ViewHolder viewHolder, ListView listView, EMConversation emConversation) {
        viewHolder.setText(R.id.message_title,emConversation.getUserName());
        viewHolder.setText(R.id.message_num,String.valueOf(emConversation.getAllMsgCount()));
        viewHolder.setText(R.id.message_info,emConversation.getLastMessage().getBody().toString());
        viewHolder.setText(R.id.message_time, DateUtils.getTimestampString(new Date(emConversation.getLastMessage().getMsgTime())));
    }

}
