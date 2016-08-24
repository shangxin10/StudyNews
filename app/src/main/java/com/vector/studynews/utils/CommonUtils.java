package com.vector.studynews.utils;

import android.text.TextUtils;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.util.HanziToPinyin;
import com.vector.studynews.config.Config;
import com.vector.studynews.entity.User;

import java.util.ArrayList;

/**
 * Created by zhang on 2016/8/22.
 */
public class CommonUtils {
    public static final String TAG = "CommonUtils";

    public static EMConversation.EMConversationType getConversationType(int chatType){
        switch (chatType){
            case Config.CHATTYPE_SINGLE:
                return EMConversation.EMConversationType.Chat;
            case Config.CHATTYPE_GROUP:
                return EMConversation.EMConversationType.GroupChat;
            case Config.CHATTYPE_CHATROOM:
                return EMConversation.EMConversationType.ChatRoom;
            default:return null;
        }
    }
    /**
     * set initial letter of according user's nickname( username if no nickname)
     *
     * @param username
     * @param user
     */
    public static void setUserInitialLetter(User user) {
        final String DefaultLetter = "#";
        String letter = DefaultLetter;

        final class GetInitialLetter {
            String getLetter(String name) {
                if (TextUtils.isEmpty(name)) {
                    return DefaultLetter;
                }
                char char0 = name.toLowerCase().charAt(0);
                if (Character.isDigit(char0)) {
                    return DefaultLetter;
                }
                ArrayList<HanziToPinyin.Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
                if (l != null && l.size() > 0 && l.get(0).target.length() > 0)
                {
                    HanziToPinyin.Token token = l.get(0);
                    String letter = token.target.substring(0, 1).toUpperCase();
                    char c = letter.charAt(0);
                    if (c < 'A' || c > 'Z') {
                        return DefaultLetter;
                    }
                    return letter;
                }
                return DefaultLetter;
            }
        }

        if ( !TextUtils.isEmpty(user.getNick()) ) {
            letter = new GetInitialLetter().getLetter(user.getNick());
            user.setInitialLetter(letter);
            return;
        }
        if (letter == DefaultLetter && !TextUtils.isEmpty(user.getUsername())) {
            letter = new GetInitialLetter().getLetter(user.getUsername());
        }
        user.setInitialLetter(letter);
    }
}
