package com.vector.studynews.utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by zhang on 2016/8/17.
 */
public class ViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;
    private int position;
    private Context context;

    public View getmConvertView(){
        return mConvertView;
    }

    public ViewHolder(Context context, ViewGroup parent,int layoutId,int position){
        this.mViews = new SparseArray<View>();
        this.context = context;
        this.position = position;
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        this.mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context,View convertView,ViewGroup parent,int layoutId,int position){
        if(null==convertView){
            return new ViewHolder(context,parent,layoutId,position);
        }else{
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.position = position;
            return viewHolder;
        }
    }

    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(null==view){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T)view;
    }

    public ViewHolder setText(int viewId,String text){
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
    public ViewHolder setImage(int viewId,String url){
        Picasso.with(context).load(url).into((ImageView) getView(viewId));
        return this;
    }

}
