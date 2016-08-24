package com.vector.studynews.view.emojicon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.vector.studynews.entity.Emojicon;

/**
 * Created by zhang on 2016/8/21.
 */
public class EmojiconMenuBase extends LinearLayout {

    protected EmojiconMenuListener listener;
    public EmojiconMenuBase(Context context) {
        super(context);
    }

    public EmojiconMenuBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmojiconMenuBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setEmojiconMenuListener(EmojiconMenuListener listener){
        this.listener = listener;
    }
    public interface  EmojiconMenuListener{
        /**
         * on emojicon clicked
         * @param emojicon
         */
        void onExpressionClicked(Emojicon emojicon);
        /**
         * on delete image clicked
         */
        void onDeleteImageClicked();
    }
}
