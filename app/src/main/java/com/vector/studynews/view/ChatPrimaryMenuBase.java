package com.vector.studynews.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Created by zhang on 2016/8/21.
 */
public abstract class ChatPrimaryMenuBase extends RelativeLayout {
    protected Activity activity;
    protected InputMethodManager inputManager;
    protected ChatPrimaryMenuListener listener;

    public ChatPrimaryMenuBase(Context context) {
        super(context);
        init(context);
    }

    public ChatPrimaryMenuBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatPrimaryMenuBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void init(Context context){
        this.activity = (Activity) context;
        inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    /**
     * set primary menu listener
     * @param listener
     */
    public void setChatPrimaryMenuListener(ChatPrimaryMenuListener listener){
        this.listener = listener;
    }

    /**
     * emoji icon input event
     * @param emojiContent
     */
    public abstract void onEmojiconInputEvent(CharSequence emojiContent);

    /**
     * emoji icon delete event
     */
    public abstract  void onEmojiconDeleteEvent();

    /**
     * hide extend menu
     */
    public abstract void onExtendMenuContainerHide();
    /**
     * insert text
     * @param text
     */
    public abstract void onTextInsert(CharSequence text);

    public abstract EditText getEditText();
    /**
     * hide keyboard
     */
    public void hideKeyboard(){
        if(activity.getWindow().getAttributes().softInputMode!= WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN){
            if(activity.getCurrentFocus()!=null){
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public interface ChatPrimaryMenuListener{
        /**
         * when send button clicked
         * @param content
         */
        void onSendBtnClicked(String content);

        /**
         * when speak button is touched
         * @param v
         * @param motionEvent
         * @return
         */
        boolean onPressToSpeakBtnTouch(View v, MotionEvent motionEvent);

        /**
         * toggle on/off voice button
         */
        void onToggleVoiceBtnClicked();

        /**
         *  toggle on/off extend menu
         */
        void onToggleExtendClicked();

        /**
         * toggle on/off emoji icon
         */
        void onToggleEmojiconClicked();

        /**
         * on text input is clicked
         */
        void onEditTextClicked();
    }
}
