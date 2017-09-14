package com.riseapps.taplor.Widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;

import com.riseapps.taplor.R;

/**
 * Created by naimish on 12/9/17.
 */

public class AnimatedButton extends AppCompatButton {
    public AnimatedButton(Context context) {
        super(context);
    }

    public AnimatedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.textanimation));

    }
}
