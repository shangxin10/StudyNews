package com.vector.studynews.controller;

import com.vector.studynews.entity.User;

/**
 * Created by zhang on 2016/8/23.
 */
public class UI {

    private static UI instance;
    private static UserProfileProvider userProfileProvider;
    private UI(){}

    public static UI getInstance(){
        if(instance==null){
            instance = new UI();
        }
        return instance;
    }

    public void setUserProfileProvider(UserProfileProvider userProfileProvider){
        this.userProfileProvider = userProfileProvider;
    }


    public UserProfileProvider getUserProfileProvider(){
        return userProfileProvider;
    }

    public interface UserProfileProvider {
        /**
         * return EaseUser for input username
         * @param username
         * @return
         */
        User getUser(String username);
    }


}
