package com.vector.studynews.utils;

import android.content.Context;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.vector.studynews.db.SharedHelper;
import com.vector.studynews.model.HXModel;

/**
 * Created by zhang on 2016/8/19.
 */
public class EMHelper {

    private static EMHelper instance = null;
    private HXModel hxModel = null;
    private String username;

    public boolean isLoggedIn(){
        return EMClient.getInstance().isLoggedInBefore();
    }

    public synchronized static EMHelper getInstance(){
        if(instance==null){
            instance = new EMHelper();
        }
        return instance;
    }
    public String getCurrentUsername(){
        if (username==null){
            return hxModel.getCurrentUsernName();
        }
        return username;
    }

    public void setCurrentUsername(String currentUsername){
        this.username = currentUsername;
        hxModel.setCurrentUserName(currentUsername);
    }


    public synchronized void init(Context context){
        hxModel = new HXModel(context);
        EMClient.getInstance().init(context,initOptions());
        EMClient.getInstance().setDebugMode(true);
        SharedHelper.init(context);
    }

    private EMOptions initOptions(){
        EMOptions emOptions = new EMOptions();
        emOptions.setAcceptInvitationAlways(false);
        return emOptions;
    }


}
