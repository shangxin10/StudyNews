package com.vector.studynews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.vector.studynews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2016/8/21.
 */
public class ChatExtendMenu extends GridView {
    protected Context context;
    private List<ChatMenuItemModel> itemModels = new ArrayList<ChatMenuItemModel>();
    public ChatExtendMenu(Context context) {
        super(context);
        init(context,null);
    }
    public ChatExtendMenu(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context,attributeSet);
    }

    public ChatExtendMenu(Context context, AttributeSet attributeSet, int defStyle){
        this(context,attributeSet);
    }

    public void init(Context context,AttributeSet attributeSet){
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.ChatExtendMenu);
        int numColumns = ta.getInt(R.styleable.ChatExtendMenu_numColumns,4);
        ta.recycle();
        setNumColumns(numColumns);
        setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        setGravity(Gravity.CENTER_VERTICAL);
        setVerticalSpacing(DensityUtil.dip2px(context,8));

    }

    public void init(){
        setAdapter(new ItemAdapter(context,itemModels));
    }
    public void registerMenuItem(String name,int drawableRes,int itemId,ChatExtendMenuItemClickListener listener){
        ChatMenuItemModel item = new ChatMenuItemModel();
        item.name = name;
        item.image = drawableRes;
        item.id = itemId;
        item.clickListener = listener;
        itemModels.add(item);
    }

    public void registerMenuItem(int nameRes,int drawableRes,int itemId,ChatExtendMenuItemClickListener listener){
        registerMenuItem(context.getString(nameRes),drawableRes,itemId,listener);
    }

    private class ItemAdapter extends ArrayAdapter<ChatMenuItemModel>{
        private Context context;
        public ItemAdapter(Context context,List<ChatMenuItemModel> objects){
            super(context,1,objects);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ChatMenuItem chatMenuItem = null;
            if(convertView==null){
                convertView = new ChatMenuItem(context);
            }
            chatMenuItem=(ChatMenuItem)convertView;
            chatMenuItem.setImageView(getItem(position).image);
            chatMenuItem.setText(getItem(position).name);
            chatMenuItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getItem(position).clickListener!=null){
                        getItem(position).clickListener.onClick(getItem(position).id,v);
                    }
                }
            });
            return convertView;
        }
    }
    public interface ChatExtendMenuItemClickListener{
        void onClick(int itemId,View view);
    }
    class ChatMenuItemModel{
        String name;
        int image;
        int id;
        ChatExtendMenuItemClickListener clickListener;
    }

    class ChatMenuItem extends LinearLayout{
        private ImageView imageView;
        private TextView textView;

        public ChatMenuItem(Context context, AttributeSet attrs, int defStyle) {
            this(context, attrs);
        }

        public ChatMenuItem(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context, attrs);
        }

        public ChatMenuItem(Context context) {
            super(context);
            init(context, null);
        }
        private void init(Context context,AttributeSet attributeSet){
            LayoutInflater.from(context).inflate(R.layout.item_chat_menu,this);
            imageView = (ImageView) findViewById(R.id.image);
            textView = (TextView) findViewById(R.id.text);
        }
        public void setImageView(int resid){
            imageView.setBackgroundResource(resid);
        }
        public void setText(int resid){
            textView.setText(resid);
        }
        public void setText(String text){
            textView.setText(text);
        }
    }


}
