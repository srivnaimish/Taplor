package com.riseapps.taplor.View;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.riseapps.taplor.Executor.CloseGameFragment;
import com.riseapps.taplor.R;
import com.riseapps.taplor.Utils.AppConstants;
import com.riseapps.taplor.Utils.SharedPreferenceSingelton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import flepsik.github.com.progress_ring.ProgressRingView;
import me.toptas.fancyshowcase.DismissListener;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    View content1,content2;
    ConstraintLayout background;
    Button easyOne, easyTwo, easyThree, timer, freeze;
    String answer = "";
    int score = 0;
    ImageButton home;
    boolean shownDemo = false;
    ConstraintLayout game;
    private int ansPos;
    private MediaPlayer correct, wrong;
    private TextView Score;
    private Animation floating;
    private AdView mAdView;
    private SharedPreferenceSingelton sharedPreferenceSingelton = new SharedPreferenceSingelton();
    private ProgressRingView progressBar;
    FancyShowCaseView fancyShowCaseView1, fancyShowCaseView2, fancyShowCaseView3;

    public DemoActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onWindowFocusChanged(true);
        setContentView(R.layout.fragment_demo);
        background = findViewById(R.id.background);
        Score = findViewById(R.id.score);
        easyOne = findViewById(R.id.easy_one);
        easyTwo = findViewById(R.id.easy_two);
        easyThree = findViewById(R.id.easy_three);
        progressBar = findViewById(R.id.progressBar);
        easyOne.setOnClickListener(this);
        easyTwo.setOnClickListener(this);
        easyThree.setOnClickListener(this);
        freeze = findViewById(R.id.time_freezer);
        floating = AppConstants.getFloatingAnimation(this);

        easyOne.startAnimation(floating);
        easyTwo.startAnimation(floating);
        easyThree.startAnimation(floating);
        home = findViewById(R.id.home);
        home.setOnClickListener(this);
        game = findViewById(R.id.easy_game);
        mAdView = findViewById(R.id.adView);
        content1=findViewById(R.id.content1);
        content2=findViewById(R.id.content2);

        easyOne.setBackgroundResource(R.drawable.starfish1);
        easyTwo.setBackgroundResource(R.drawable.starfish1);
        easyThree.setBackgroundResource(R.drawable.starfish1);

        if (AppConstants.paid3 || AppConstants.paid4) {
            mAdView.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mAdView.loadAd(adRequest);
        }

        correct = MediaPlayer.create(this,R.raw.correct);
        wrong = MediaPlayer.create(this, R.raw.wrong);

        fancyShowCaseView1 = new FancyShowCaseView.Builder(this)
                .focusOn(game)
                .title(getString(R.string.app_walk1))
                .closeOnTouch(true)
                .disableFocusAnimation()
                .dismissListener(new DismissListener() {
                    @Override
                    public void onDismiss(String id) {
                        changeColors();
                    }

                    @Override
                    public void onSkipped(String id) {

                    }
                })
                .titleGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                .titleSize((int) getResources().getDimension(R.dimen.size13sp), 1)
                .backgroundColor(Color.parseColor("#F221242B"))
                .build();

        fancyShowCaseView2 = new FancyShowCaseView.Builder(this)
                .focusOn(content1)
                .title(getString(R.string.app_walk2))
                .closeOnTouch(true)
                .dismissListener(new DismissListener() {
                    @Override
                    public void onDismiss(String id) {
                        changeColors();
                    }

                    @Override
                    public void onSkipped(String id) {

                    }
                })
                .titleSize((int) getResources().getDimension(R.dimen.size13sp), 1)
                .backgroundColor(Color.parseColor("#F221242B"))
                .titleGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                .build();

        fancyShowCaseView3 = new FancyShowCaseView.Builder(this)
                .focusOn(content2)
                .title(getString(R.string.app_walk3))
                .closeOnTouch(true)
                .dismissListener(new DismissListener() {
                    @Override
                    public void onDismiss(String id) {
                        changeColors();
                    }

                    @Override
                    public void onSkipped(String id) {

                    }
                })
                .backgroundColor(Color.parseColor("#F221242B"))
                .titleSize((int) getResources().getDimension(R.dimen.size13sp), 1)
                .titleGravity(Gravity.CENTER)
                .build();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new FancyShowCaseQueue()
                        .add(fancyShowCaseView1)
                        .add(fancyShowCaseView2)
                        .add(fancyShowCaseView3)
                        .show();
                changeColors();
            }
        },1000);

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.easy_one:
                if (answer.equalsIgnoreCase(easyOne.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(this, view));
                    onCorrectAnswer();
                } else {
                    wrong.start();
                    Score.setText("0");
                    //gameOver();
                }
                break;

            case R.id.easy_two:
                if (answer.equalsIgnoreCase(easyTwo.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(this, view));
                    onCorrectAnswer();
                } else {
                    Score.setText("0");
                    wrong.start();
                }
                break;

            case R.id.easy_three:
                if (answer.equalsIgnoreCase(easyThree.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(this, view));
                    onCorrectAnswer();
                } else {
                    Score.setText("0");
                    wrong.start();
                }
                break;

            case R.id.home:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //closeFragment.closeFragment();
                break;


        }
    }

    private void changeColors() {

        ArrayList<String> namelist = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            namelist.add(AppConstants.easyColorNames[i]);
        }
        Collections.shuffle(namelist);

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


        if (!shownDemo) {

            shownDemo = true;
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }else {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
