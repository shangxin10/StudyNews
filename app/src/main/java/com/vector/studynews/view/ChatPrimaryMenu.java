package com.vector.studynews.view;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vector.studynews.R;

/**
 * Created by zhang on 2016/8/21.
 */
public class ChatPrimaryMenu extends ChatPrimaryMenuBase implements View.OnClickListener{

    private EditText editText;
    private View buttonSetModeKeyboard;
    private RelativeLayout edittext_layout;
    private View buttonSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    private ImageView faceNormal;
    private ImageView faceChecked;
    private Button buttonMore;
    private RelativeLayout faceLayout;
    private Context context;
    public ChatPrimaryMenu(Context context) {
        super(context);
        init(context,null);
    }

    public ChatPrimaryMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ChatPrimaryMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attributeSet){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.widget_chat_primary_menu,this);
        editText = (EditText) findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        faceLayout = (RelativeLayout) findViewById(R.id.rl_face);
        faceChecked = (ImageView) findViewById(R.id.iv_face_checked);
        faceNormal = (ImageView) findViewById(R.id.iv_face_normal);
        buttonMore = (Button) findViewById(R.id.btn_more);

        edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);

        buttonSend.setOnClickListener(this);
        buttonPressToSpeak.setOnClickListener(this);
        faceLayout.setOnClickListener(this);
        buttonMore.setOnClickListener(this);
        buttonSetModeVoice.setOnClickListener(this);
        buttonSetModeKeyboard.setOnClickListener(this);
        editText.setOnClickListener(this);
        editText.requestFocus();
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                }else{
                    edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!TextUtils.isEmpty(s)){
                    buttonMore.setVisibility(INVISIBLE);
                    buttonSend.setVisibility(VISIBLE);
                }else{
                    buttonSend.setVisibility(INVISIBLE);
                    buttonMore.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        buttonPressToSpeak.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(listener!=null){
                    return listener.onPressToSpeakBtnTouch(v,event);
                }
                return false;
            }
        });
    }

    /**
     * append emoji icon to editText
     * @param emojiContent
     */
    @Override
    public void onEmojiconInputEvent(CharSequence emojiContent) {
        editText.append(emojiContent);
    }

    /**
     * delete emojicon
     */
    @Override
    public void onEmojiconDeleteEvent() {
        if(!TextUtils.isEmpty(editText.getText())){
            KeyEvent event = new KeyEvent(0,0,0,KeyEvent.KEYCODE_DEL,0,0,0,0,KeyEvent.KEYCODE_ENDCALL);
            editText.dispatchKeyEvent(event);
        }
    }

    @Override
    public void onExtendMenuContainerHide() {
        showNormalFaceImage();
    }

    @Override
    public void onTextInsert(CharSequence text) {
        int start = editText.getSelectionStart();
        Editable editable = editText.getEditableText();
        editable.insert(start,text);
        setModeKeyboard();
    }

    @Override
    public EditText getEditText() {
        return editText;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.btn_send:
                if(listener !=null){
                    String s = editText.getText().toString();
                    editText.setText("");
                    listener.onSendBtnClicked(s);
                }
                break;
            case R.id.btn_set_mode_voice:
                setModeVoice();
                showNormalFaceImage();
                if(listener!=null){
                    listener.onToggleVoiceBtnClicked();
                }
                break;
            case R.id.btn_set_mode_keyboard:
                setModeKeyboard();
                showNormalFaceImage();
                if(listener!=null){
                    listener.onToggleVoiceBtnClicked();
                }
                break;
            case R.id.btn_more:
                buttonSetModeVoice.setVisibility(VISIBLE);
                buttonSetModeKeyboard.setVisibility(GONE);
                edittext_layout.setVisibility(VISIBLE);
                buttonPressToSpeak.setVisibility(GONE);
                showNormalFaceImage();
                if(listener!=null){
                    listener.onToggleExtendClicked();
                }
                break;
            case R.id.et_sendmessage:
                edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
                faceNormal.setVisibility(VISIBLE);
                faceChecked.setVisibility(INVISIBLE);
                if(listener!=null){
                    listener.onEditTextClicked();
                }
                break;
            case R.id.rl_face:
                toggleFaceImage();
                if(listener!=null){
                    listener.onToggleEmojiconClicked();
                }
                break;
            default:break;

        }
    }

    protected void setModeVoice(){
        hideKeyboard();
        edittext_layout.setVisibility(GONE);
        buttonSetModeVoice.setVisibility(GONE);
        buttonSetModeKeyboard.setVisibility(VISIBLE);
        buttonSend.setVisibility(GONE);
        buttonMore.setVisibility(VISIBLE);
        buttonPressToSpeak.setVisibility(VISIBLE);
        faceNormal.setVisibility(VISIBLE);
        faceChecked.setVisibility(GONE);
    }
    protected void setModeKeyboard(){
        edittext_layout.setVisibility(VISIBLE);
        buttonSetModeKeyboard.setVisibility(GONE);
        buttonSetModeVoice.setVisibility(VISIBLE);

        buttonPressToSpeak.setVisibility(GONE);
        editText.requestFocus();
        if(!TextUtils.isEmpty(editText.getText())){
            buttonMore.setVisibility(GONE);
            buttonSend.setVisibility(VISIBLE);
        }else{
            buttonMore.setVisibility(VISIBLE);
            buttonSend.setVisibility(GONE);
        }
    }

    private void showNormalFaceImage(){
        faceNormal.setVisibility(View.VISIBLE);
        faceChecked.setVisibility(INVISIBLE);
    }
    private void showSelectedFaceImage(){
        faceNormal.setVisibility(INVISIBLE);
        faceChecked.setVisibility(VISIBLE);
    }

    protected void toggleFaceImage(){
        if(faceNormal.getVisibility() == VISIBLE){
            showSelectedFaceImage();
        }else{
            showNormalFaceImage();
        }
    }
}
