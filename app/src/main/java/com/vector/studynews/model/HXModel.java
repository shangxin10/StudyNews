package com.vector.studynews.model;

import android.content.Context;

import com.vector.studynews.db.SharedHelper;

/**
 * Created by zhang on 2016/8/19.
 */


public class HXModel {


    private static final String SHARED_USERNAME_KEY = "username";


    public HXModel(Context context){

    }
    public void setCurrentUserName(String username){
        SharedHelper.getInstance().save(SHARED_USERNAME_KEY,username);
    }

    public String getCurrentUsernName(){
        return (String) SharedHelper.getInstance().read(SHARED_USERNAME_KEY,"");
    }
}
