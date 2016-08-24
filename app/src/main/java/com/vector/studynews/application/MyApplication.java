package com.vector.studynews.application;

import android.app.Application;
import android.content.Context;

import com.vector.studynews.utils.EMHelper;

/**
 * Created by zhang on 2016/8/18.
 */
public class MyApplication extends Application {

    private static Context applicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
        this.applicationContext = getApplicationContext();
        EMHelper.getInstance().init(applicationContext);
    }
    public static Context getApplication(){
        return applicationContext;
    }
}
