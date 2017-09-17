package com.riseapps.taplor.View;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.riseapps.taplor.Executor.CloseGameFragment;
import com.riseapps.taplor.R;
import com.riseapps.taplor.Utils.AppConstants;
import com.riseapps.taplor.Utils.SharedPreferenceSingelton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class DemoFragment extends Fragment implements View.OnClickListener {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    ConstraintLayout background;
    Button easyOne, easyTwo, easyThree,timer,freeze;
    String answer = "";
    int score = 0;
    private int ansPos;
    private MediaPlayer correct, wrong;
    private TextView Score;
    private Animation floating;
    boolean shownDemo=false;
    ConstraintLayout game;
    private AdView mAdView;
    private SharedPreferenceSingelton sharedPreferenceSingelton=new SharedPreferenceSingelton();

    public DemoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        View view = inflater.inflate(R.layout.fragment_demo, container, false);
        background = view.findViewById(R.id.background);
        Score = view.findViewById(R.id.score);

        easyOne = view.findViewById(R.id.easy_one);
        easyTwo = view.findViewById(R.id.easy_two);
        easyThree = view.findViewById(R.id.easy_three);

        easyOne.setOnClickListener(this);
        easyTwo.setOnClickListener(this);
        easyThree.setOnClickListener(this);
        timer=view.findViewById(R.id.timer);
        freeze=view.findViewById(R.id.time_freezer);
        floating=AppConstants.getFloatingAnimation(getContext());

        easyOne.startAnimation(floating);
        easyTwo.startAnimation(floating);
        easyThree.startAnimation(floating);

        game=view.findViewById(R.id.easy_game);

        mAdView = view.findViewById(R.id.adView);
        /*if(!sharedPreferenceSingelton.getSavedBoolean(getActivity(),"Payment")) {
            AdRequest adRequest = new AdRequest.Builder()
                    //.addTestDevice("1BB6AD3C4E832E63122601E2E4752AF4")
                    .build();
            mAdView.loadAd(adRequest);
        }else {
            mAdView.setVisibility(View.GONE);
        }*/
        changeColors();

        correct = MediaPlayer.create(getContext(), R.raw.correct);
        wrong = MediaPlayer.create(getContext(), R.raw.wrong);


        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.easy_one:
                if (answer.equalsIgnoreCase(easyOne.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    wrong.start();
                    //gameOver();
                }
                break;

            case R.id.easy_two:
                if (answer.equalsIgnoreCase(easyTwo.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    //gameOver();
                    wrong.start();
                }
                break;

            case R.id.easy_three:
                if (answer.equalsIgnoreCase(easyThree.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    //gameOver();
                    wrong.start();
                }
                break;


        }
    }

    private void changeColors() {

        ArrayList<Integer> colorslist = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            colorslist.add(AppConstants.splashBackground[i]);
        }
        Collections.shuffle(colorslist);

        ArrayList<String> namelist = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            namelist.add(AppConstants.easyColorNames[i]);
        }
        Collections.shuffle(namelist);

        easyOne.setBackgroundResource(colorslist.get(0));
        easyTwo.setBackgroundResource(colorslist.get(1));
        easyThree.setBackgroundResource(colorslist.get(2));

        easyOne.setText(namelist.get(0));
        easyTwo.setText(namelist.get(1));
        easyThree.setText(namelist.get(2));

        Random random = new Random();
        answer = namelist.get(random.nextInt(3));
        ansPos = Arrays.asList(AppConstants.easyColorNames).indexOf(answer);

        int colorFrom = Color.TRANSPARENT;
        Drawable back = background.getBackground();
        if (back instanceof ColorDrawable)
            colorFrom = ((ColorDrawable) back).getColor();
        int colorTo = getResources().getColor(AppConstants.easyColorCodes[ansPos]);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(400); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                background.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setNavigationBarColor(getResources().getColor(AppConstants.easyColorCodes[ansPos]));
        }

        if(!shownDemo){
            new TapTargetSequence(getActivity()).targets(
                    TapTarget.forView(timer, getString(R.string.app_walk1))
                            .dimColor(android.R.color.black)
                            .outerCircleColor(R.color.PURPLE)
                            .targetCircleColor(R.color.buttonTextColor)
                            .textTypeface(Typeface.SANS_SERIF)
                            .transparentTarget(true)
                            .textColor(R.color.buttonTextColor)
                            .targetRadius(90)
                            .id(1),
                    TapTarget.forView(freeze, getString(R.string.app_walk2))
                            .dimColor(android.R.color.black)
                            .outerCircleColor(R.color.CHOCOLATE)
                            .targetCircleColor(R.color.buttonTextColor)
                            .textTypeface(Typeface.SANS_SERIF)
                            .transparentTarget(true)
                            .textColor(R.color.buttonTextColor)
                            .targetRadius(110)
                            .id(2),
                    TapTarget.forView(game, getString(R.string.app_walk3)+" "+answer+". "+getString(R.string.app_walk4))
                            .dimColor(android.R.color.black)
                            .outerCircleColor(R.color.PEACH)
                            .targetCircleColor(R.color.buttonTextColor)
                            .textTypeface(Typeface.SANS_SERIF)
                            .transparentTarget(true)
                            .textColor(R.color.buttonTextColor)
                            .targetRadius(120)
                            .id(2)
            ).start();
            shownDemo=true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    private void onCorrectAnswer() {
        score++;
        Score.setText("" + score);
        changeColors();
        correct.start();
    }

}
