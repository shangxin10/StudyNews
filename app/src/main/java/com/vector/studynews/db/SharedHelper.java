package com.vector.studynews.db;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhang on 2016/8/18.
 */
public class SharedHelper  {
    public static final String FILE_NAME = "studynews";

    private static SharedHelper sharedHelper;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private SharedHelper(Context context){
        sharedPreferences = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public synchronized static void init(Context context){
        if(sharedHelper==null){
            sharedHelper = new SharedHelper(context);
        }

    }

    public synchronized static SharedHelper getInstance(){
        if(sharedHelper==null){
            throw  new RuntimeException("please init first");
        }else{
            return sharedHelper;
        }
    }

    public void save( String key, Object obj){

        if(obj instanceof Boolean){
            editor.putBoolean(key,(Boolean)obj);
        }else if(obj instanceof Float){
            editor.putFloat(key,(Float)obj);
        }else if(obj instanceof Integer){
            editor.putInt(key,(Integer)obj);
        }else if(obj instanceof Long){
            editor.putLong(key,(Long)obj);
        }else {
            editor.putString(key,(String)obj);
        }
        editor.commit();
    }
    public Object read(String key,Object defaultValue){

        if(defaultValue instanceof Boolean){
            return sharedPreferences.getBoolean(key,(Boolean)defaultValue);
        }else if(defaultValue instanceof Float){
            return sharedPreferences.getFloat(key,(Float)defaultValue);
        }else if(defaultValue instanceof Integer){
            return sharedPreferences.getInt(key,(Integer)defaultValue);
        }else if(defaultValue instanceof Long){
            return sharedPreferences.getLong(key,(Long)defaultValue);
        }else if(defaultValue instanceof String){
            return sharedPreferences.getString(key,(String)defaultValue);
        }
        return null;
    }

    public void clear(Context context){

        editor.clear();
        editor.commit();
    }

    public void remove(Context context,String key){

        editor.remove(key);
        editor.commit();
    }

    public boolean contains(Context context,String key){

        return sharedPreferences.contains(key);
    }
}
