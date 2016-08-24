package com.vector.studynews.ui.fragment;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.vector.studynews.R;
import com.vector.studynews.adapter.MessageAdapter;
import com.vector.studynews.config.Config;
import com.vector.studynews.model.HXModel;
import com.vector.studynews.ui.activity.ChatActivity;
import com.vector.studynews.ui.activity.MainActivity;
import com.vector.studynews.utils.EMHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang on 2016/8/16.
 */
public class MessageFragment extends Fragment {
    private ListView lv_message;
    private List<EMConversation> conversationList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,null);

        lv_message = (ListView) view.findViewById(R.id.lv_message);
        conversationList = loadConversationList();
        MessageAdapter messageAdapter = new MessageAdapter(getContext(),conversationList,R.layout.item_message);
        lv_message.setAdapter(messageAdapter);
        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationList.get(position);
                String username = conversation.getUserName();
                if(username.equals(EMHelper.getInstance().getCurrentUsername())){
                    Toast.makeText(getActivity(),"不能跟自己聊天",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(getActivity(), ChatActivity.class);
                if(conversation.isGroup()){
                    if(conversation.getType()== EMConversation.EMConversationType.ChatRoom){
                        i.putExtra(Config.EXTRA_CHAR_TYPE,Config.CHATTYPE_CHATROOM);
                    }else{
                        i.putExtra(Config.EXTRA_CHAR_TYPE,Config.CHATTYPE_GROUP);
                    }
                }
                i.putExtra(Config.EXTRA_USER_ID,conversation.getUserName());
                startActivity(i);

            }
        });
        return view;
    }

    /**
     * 加载会话列表
     * @return
     */
    public List<EMConversation> loadConversationList(){
        Map<String,EMConversation> conversationMap = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long,EMConversation>> sortList = new ArrayList<Pair<Long,EMConversation>>();
        synchronized (conversationMap){
            for(EMConversation conversation:conversationMap.values()){
                if(conversation.getAllMessages().size()!=0){
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(),conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for(Pair<Long,EMConversation> sortItem : sortList){
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 改变会话的顺序
     * @param conversationList
     */
    public void sortConversationByLastChatTime(List<Pair<Long,EMConversation>> conversationList){
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(Pair<Long, EMConversation> con1, Pair<Long, EMConversation> con2) {
                if(con1.first==con2.first){
                    return 0;
                }else if(con2.first>con1.first){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
    }
}
