package com.riseapps.taplor.Utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.riseapps.taplor.R;

/**
 * Created by naimish on 18/8/17.
 */

public class AppConstants {

    /*Color Constants*/

    public static final int[] splashBackground = {R.drawable.splash_red, R.drawable.splash_blue, R.drawable.splash_green, R.drawable.splash_yellow, R.drawable.splash_black, R.drawable.splash_orange, R.drawable.splash_brown, R.drawable.splash_indigo, R.drawable.splash_pink, R.drawable.splash_purple, R.drawable.splash_grey, R.drawable.splash_teal, R.drawable.splash_lime, R.drawable.splash_midnight_blue, R.drawable.splash_maroon};

    public static final String[] easyColorNames = {"Red", "Blue", "Green", "Yellow", "Black"};

    public static final String[] mediumColorNames = {"Red", "Blue", "Green", "Yellow", "Black", "Orange", "Brown", "Indigo", "Pink", "Purple"};

    public static final String[] hardColorNames = {"Red", "Blue", "Green", "Yellow", "Black", "Orange", "Brown", "Indigo", "Pink", "Purple", "Grey", "Teal", "Lime", "Deep Blue", "Maroon"};

    public static final int[] easyColorCodes = {R.color.RED, R.color.BLUE, R.color.GREEN, R.color.YELLOW, R.color.BLACK};

    public static final int[] mediumColorCodes = {R.color.RED, R.color.BLUE, R.color.GREEN, R.color.YELLOW, R.color.BLACK, R.color.ORANGE, R.color.BROWN, R.color.INDIGO, R.color.PINK, R.color.PURPLE};

    public static final int[] hardColorCodes = {R.color.RED, R.color.BLUE, R.color.GREEN, R.color.YELLOW, R.color.BLACK, R.color.ORANGE, R.color.BROWN, R.color.INDIGO, R.color.PINK, R.color.PURPLE, R.color.GREY, R.color.TEAL, R.color.LIME, R.color.DEEP_BLUE, R.color.MAROON};

    public static AnimationSet generateFadeInAnimator(int l1, int l2) {
        AnimationSet set = new AnimationSet(true);
        Animation trAnimation = new TranslateAnimation(0, 0, -100, 0);
        trAnimation.setDuration(l1);

        trAnimation.setRepeatMode(Animation.REVERSE);
        set.addAnimation(trAnimation);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(l2);
        set.addAnimation(anim);
        return set;
    }

    public static AnimationSet generateBottomUpFadeInAnimator() {
        AnimationSet set = new AnimationSet(true);
        Animation trAnimation = new TranslateAnimation(0, 0, 100, 0);
        trAnimation.setDuration(2000);

        trAnimation.setRepeatMode(Animation.REVERSE);
        set.addAnimation(trAnimation);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2000);
        set.addAnimation(anim);
        return set;
    }

    public static AnimationSet dialogEnter() {
        AnimationSet set = new AnimationSet(true);
        Animation trAnimation = new TranslateAnimation(0, 0, 500, 0);
        trAnimation.setDuration(700);

        trAnimation.setRepeatMode(Animation.REVERSE);
        set.addAnimation(trAnimation);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        set.addAnimation(anim);
        return set;
    }

    public static AnimationSet dialogExit() {
        AnimationSet set = new AnimationSet(true);
        Animation trAnimation = new TranslateAnimation(0, 0, 0, 500);
        trAnimation.setDuration(700);

        trAnimation.setRepeatMode(Animation.REVERSE);
        trAnimation.setFillAfter(true);
        set.addAnimation(trAnimation);
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(1000);
        set.addAnimation(anim);
        return set;
    }

    public static final int[] GradientPrimaryColors = {R.color.ONE, R.color.THREE, R.color.FIVER, R.color.SEVEN, R.color.NINE};

    public static final String FB_URL = "https://www.facebook.com";

    public static final String TWITTER_URL = "https://www.twitter.com";

    public static Animation getFloatingAnimation(Context context){
        return AnimationUtils.loadAnimation(context,R.anim.floating);
    }

    public static Animation getBubbleAnimation(final Context context, final View view){
        Animation animation=AnimationUtils.loadAnimation(context,R.anim.bubble);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(getFloatingAnimation(context));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }

    public static AnimationSet generateFloatFadeAnimator(int l1, int l2, final View view, final Context context) {
        AnimationSet set = new AnimationSet(true);
        Animation trAnimation = new TranslateAnimation(0, 0, -100, 0);
        trAnimation.setDuration(l1);

        trAnimation.setRepeatMode(Animation.REVERSE);
        set.addAnimation(trAnimation);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(l2);
        set.addAnimation(anim);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(AnimationUtils.loadAnimation(context,R.anim.floating2));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return set;
    }

}
