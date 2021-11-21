package com.example.kirshokerbondhu.classes;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;

public class LoadingTyper extends androidx.appcompat.widget.AppCompatTextView {

    private static final String TAG = LoadingTyper.class.getSimpleName();
    private CharSequence mText;
    private int mIndex;
    private long mDelay = 500; // Default 500ms character delay
    Handler animationCompleteCallBack;

    public LoadingTyper(Context context) {
        super(context);
    }

    public void setAnimationCompleteListener(Handler animationCompleteCallBack) {
        this.animationCompleteCallBack = animationCompleteCallBack;
    }

    public LoadingTyper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler mHandler = new Handler();
    private final Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            if (mIndex++ < mText.length())
                setText(mText.subSequence(0, mIndex++));
            if (mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            } else {
                if (null != animationCompleteCallBack)
                    animationCompleteCallBack.sendMessage(new Message());
                else
                    Log.i(TAG, "Animation Complete Listener not set...");
            }
        }
    };

    public void animateText(CharSequence text) {
        mHandler = new Handler();
        mText = text;
        mIndex = 0;
        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }
}