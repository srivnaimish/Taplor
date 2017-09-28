package com.riseapps.taplor.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
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

    public static final int[] starFish = {R.drawable.starfish1,R.drawable.starfish2,R.drawable.starfish3,R.drawable.starfish4,R.drawable.starfish5,R.drawable.starfish6,R.drawable.starfish7,R.drawable.starfish8,R.drawable.starfish9,R.drawable.starfish10,R.drawable.starfish11,R.drawable.starfish12,R.drawable.starfish13,R.drawable.starfish14,R.drawable.starfish15,R.drawable.starfish16,R.drawable.starfish17,R.drawable.starfish18};

    public static final String[] easyColorNames = {"Red", "Blue", "Green", "Yellow", "Black"};

    public static final String[] mediumColorNames = {"Red", "Blue", "Green", "Yellow", "Black", "Orange", "Brown", "Indigo", "Pink", "Purple"};

    public static final String[] hardColorNames = {"Red", "Blue", "Green", "Yellow", "Black", "Orange", "Brown", "Indigo", "Pink", "Purple", "Grey", "Olive", "Peach", "TEAL", "Maroon"};

    public static final int[] easyColorCodes = {R.color.RED, R.color.BLUE, R.color.GREEN, R.color.YELLOW, R.color.BLACK};

    public static final int[] mediumColorCodes = {R.color.RED, R.color.BLUE, R.color.GREEN, R.color.YELLOW, R.color.BLACK, R.color.ORANGE, R.color.BROWN, R.color.INDIGO, R.color.PINK, R.color.PURPLE};

    public static final int[] hardColorCodes = {R.color.RED, R.color.BLUE, R.color.GREEN, R.color.YELLOW, R.color.BLACK, R.color.ORANGE, R.color.BROWN, R.color.INDIGO, R.color.PINK, R.color.PURPLE, R.color.GREY, R.color.OLIVE, R.color.PEACH, R.color.TEAL, R.color.MAROON};
    public static final String KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr821xbTS2bc6dxwlDFtEY31SOkLX7hgrPEYqGp6LgnbNrb2exrThD08bEuPonIoe7X1LRjVM5XxD/yGV4rTZ2XxCJjhTSzOCx05IGBd9cgzEMpHr7OD1215dxbS9axzM6bVw+Hn0AixgAtsPEANWFP5GWZ1XHBGbvEmxIwBlcJjj/V3pN/g1PeLARP6/rzVgjX2zr6Y+4EXsNm9bwMF0sS0n8XX3uhUQUwB2Y6dgi1Fm1ZizV7VDphIpt1FCE89NrRMmbjbLgYWdcsl1Vx/iCrlaDzjb+JAHTXEb069lrJHhUg4wSYwFOViKppGMS4nNRra019REVQRDkhfaSyp6DwIDAQAB";

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

    public static AnimationSet generateBottomUpFadeInAnimator(Context context) {
        AnimationSet set = new AnimationSet(true);
        Animation trAnimation = new TranslateAnimation(0, 0, 100, 0);
        trAnimation.setDuration(2000);
        trAnimation.setRepeatMode(Animation.REVERSE);
        set.addAnimation(trAnimation);
        Animation animation=AnimationUtils.loadAnimation(context, R.anim.floating2);;
        set.addAnimation(animation);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(2000);
        set.addAnimation(anim);
        return set;
    }

    public static AnimationSet dialogEnter(int t,int a) {
        AnimationSet set = new AnimationSet(true);
        Animation trAnimation = new TranslateAnimation(0, 0, 800, 0);
        trAnimation.setDuration(t);

        trAnimation.setRepeatMode(Animation.REVERSE);
        set.addAnimation(trAnimation);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(a);
        set.addAnimation(anim);
        return set;
    }

    public static AnimationSet dialogExit() {
        AnimationSet set = new AnimationSet(true);
        Animation trAnimation = new TranslateAnimation(0, 0, 0, 800);
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

    public static final String FB_URL = "https://www.facebook.com/taplorgame";

    public static final String TWITTER_URL = "https://twitter.com/riseandroidapps";

    public static Animation getFloatingAnimation(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.floating);
    }

    public static Animation getFloatingAnimation2(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.floating2);
    }

    public static Animation getBubbleAnimation(final Context context, final View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.bubble);
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

    public static String products[] = {"premium1", "premium2", "premium3", "premium4"};

    public static boolean paid1 = false;
    public static boolean paid2 = false;
    public static boolean paid3 = false;
    public static boolean paid4 = false;

    public static final int[] rankImages = {R.drawable.ic_1,R.drawable.ic_2,R.drawable.ic_3,R.drawable.ic_4,R.drawable.ic_5,R.drawable.ic_6,R.drawable.ic_7,R.drawable.ic_8,R.drawable.ic_9,R.drawable.ic_10};


}
