package com.vector.studynews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vector.studynews.utils.EMHelper;

/**
 * Created by zhang on 2016/8/19.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private static DBOpenHelper dbOpenHelper;
    public DBOpenHelper(Context context){
        super(context,getUserDatabaseName(),null,DATABASE_VERSION);
    }

    public static DBOpenHelper getInstance(Context context){
        if(dbOpenHelper==null){
            dbOpenHelper = new DBOpenHelper(context);
        }
        return dbOpenHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static String getUserDatabaseName(){
        return EMHelper.getInstance().getCurrentUsername()+"_hx.db";
    }
}
