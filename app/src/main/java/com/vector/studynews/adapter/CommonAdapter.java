package com.vector.studynews.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.vector.studynews.utils.ViewHolder;

import java.util.List;

/**
 * Created by zhang on 2016/8/17.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    private Context context;
    private List<T> datas;
    private int layoutId;
    private LayoutInflater mInflater;

    public CommonAdapter(Context context, List<T> datas, int layoutId){
        Log.d("commonadapter","======");
        this.context = context;
        this.datas = datas;
        this.layoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("getView","=======");
        ViewHolder holder = ViewHolder.get(context,convertView,parent,layoutId,position);
        convert(position,holder,(ListView)parent, (T) getItem(position));
        return holder.getmConvertView();
    }

    public abstract void convert(int position, ViewHolder viewHolder,ListView listView,T t);
}
