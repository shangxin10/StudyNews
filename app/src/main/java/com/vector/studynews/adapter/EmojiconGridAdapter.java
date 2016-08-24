package com.vector.studynews.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vector.studynews.R;
import com.vector.studynews.entity.Emojicon;
import com.vector.studynews.utils.SmileUtils;

import java.util.List;

/**
 * Created by zhang on 2016/8/21.
 */
public class EmojiconGridAdapter extends ArrayAdapter<Emojicon> {
    private static final String TAG = "EmojiconGridAdapter";
    private Emojicon.Type emojiconType;
    private List<Emojicon> emojiconList;
    public EmojiconGridAdapter(Context context, int resource, List<Emojicon> objects,Emojicon.Type emojiconType) {
        super(context, resource, objects);
        this.emojiconList = objects;
        this.emojiconType = emojiconType;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG,"getView");
        if(convertView == null){
            if(emojiconType== Emojicon.Type.BIG_EXPRESSION){
                convertView = View.inflate(getContext(), R.layout.row_big_expression,null);
            }else{
                convertView = View.inflate(getContext(),R.layout.row_expression,null);
            }
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.iv_expression);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_name);
        Emojicon emojicon = getItem(position);
        if(textView !=null && emojicon.getName()!=null){
            textView.setText(emojicon.getName());
        }
        if(SmileUtils.DELETE_KEY.equals(emojicon.getEmojiText())){
            imageView.setImageResource(R.mipmap.delete_expression);
        }else{
            if(emojicon.getIcon()!=0){
                imageView.setImageResource(emojicon.getIcon());
            }else if(emojicon.getIconPath()!=null){
                Glide.with(getContext()).load(emojicon.getIconPath()).placeholder(R.mipmap.default_expression).into(imageView);
            }
        }

        return convertView;
    }
}
