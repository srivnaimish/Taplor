package com.riseapps.taplor.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gelitenight.waveview.library.WaveView;
import com.google.android.gms.games.Games;
import com.riseapps.taplor.Executor.CloseGameFragment;
import com.riseapps.taplor.R;
import com.riseapps.taplor.Utils.AppConstants;
import com.riseapps.taplor.Utils.SharedPreferenceSingelton;
import com.riseapps.taplor.Utils.WaveHelper;
import com.riseapps.taplor.Widgets.MyToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import flepsik.github.com.progress_ring.ProgressRingView;

public class GameFragment extends Fragment implements View.OnClickListener {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    private static final String LEVEL = "LEVEL";

    LinearLayout easy_game, medium_game, hard_game;
    ConstraintLayout background;
    Button easyOne, easyTwo, easyThree;
    Button mediumOne, mediumTwo, mediumThree, mediumFour;
    Button hardOne, hardTwo, hardThree, hardFour, hardFive;
    String answer = "";
    ImageButton home;
    ProgressRingView progressBar;
    MyCountDownTimer countDownTimer;
    int score = 0;
    int totalTime = 5000;
    CloseGameFragment closeGameFragment;
    long currentTimeLeft;
    private int level,powerups=3;
    private int ansPos;
    private Button freeze;
    boolean stopped = false;
    private MediaPlayer correct, wrong;
    private TextView Score,ans;
    private Handler handler = new Handler();
    boolean gameOver=false;
    private int mBorderColor = Color.parseColor("#1AFFFFFF");

    private CardView game_over_dialog;
    private TextView dialog_score;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            level = getArguments().getInt(LEVEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        closeGameFragment = (CloseGameFragment) getActivity();
        background = view.findViewById(R.id.background);
        Score = view.findViewById(R.id.score);
        easy_game = view.findViewById(R.id.easy_game);
        medium_game = view.findViewById(R.id.medium_game);
        hard_game = view.findViewById(R.id.hard_game);

        easyOne = view.findViewById(R.id.easy_one);
        easyTwo = view.findViewById(R.id.easy_two);
        easyThree = view.findViewById(R.id.easy_three);

        mediumOne = view.findViewById(R.id.medium_one);
        mediumTwo = view.findViewById(R.id.medium_two);
        mediumThree = view.findViewById(R.id.medium_three);
        mediumFour = view.findViewById(R.id.medium_four);

        hardOne = view.findViewById(R.id.hard_one);
        hardTwo = view.findViewById(R.id.hard_two);
        hardThree = view.findViewById(R.id.hard_three);
        hardFour = view.findViewById(R.id.hard_four);
        hardFive = view.findViewById(R.id.hard_five);

        home = view.findViewById(R.id.home);
        freeze = view.findViewById(R.id.time_freezer);
        progressBar = view.findViewById(R.id.progressBar);

        game_over_dialog=view.findViewById(R.id.game_over_dialog);
        dialog_score=view.findViewById(R.id.new_score);
        Button leaderboard = view.findViewById(R.id.high_score);
        Button try_again = view.findViewById(R.id.try_again);
        Button share_score = view.findViewById(R.id.share);
        ImageView close_dialog = view.findViewById(R.id.close_dialog);
        ans=view.findViewById(R.id.ans);

        WaveView waveView = view.findViewById(R.id.wave);
        waveView.setBorder(0, mBorderColor);

        WaveHelper mWaveHelper = new WaveHelper(waveView);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        waveView.setWaveColor(
                Color.parseColor("#0DFFFFFF"),
                Color.parseColor("#0DFFFFFF"));
        mWaveHelper.start();
        correct = MediaPlayer.create(getContext(), R.raw.correct);
        wrong = MediaPlayer.create(getContext(), R.raw.wrong);
        checkPayment();

        easyOne.setOnClickListener(this);
        easyTwo.setOnClickListener(this);
        easyThree.setOnClickListener(this);

        mediumOne.setOnClickListener(this);
        mediumTwo.setOnClickListener(this);
        mediumThree.setOnClickListener(this);
        mediumFour.setOnClickListener(this);

        hardOne.setOnClickListener(this);
        hardTwo.setOnClickListener(this);
        hardThree.setOnClickListener(this);
        hardFour.setOnClickListener(this);
        hardFive.setOnClickListener(this);

        freeze.setOnClickListener(this);
        home.setOnClickListener(this);

        leaderboard.setOnClickListener(this);
        try_again.setOnClickListener(this);
        share_score.setOnClickListener(this);
        close_dialog.setOnClickListener(this);

        Animation floating = AppConstants.getFloatingAnimation(getContext());
        Animation floating2 = AppConstants.getFloatingAnimation2(getContext());

        switch (level) {
            case 0:
                easy_game.setVisibility(View.VISIBLE);
                easyOne.startAnimation(floating);
                easyTwo.startAnimation(floating);
                easyThree.startAnimation(floating);

                break;
            case 1:
                medium_game.setVisibility(View.VISIBLE);
                mediumOne.startAnimation(floating);
                mediumTwo.startAnimation(floating);
                mediumThree.startAnimation(floating);
                mediumFour.startAnimation(floating);

                break;
            case 2:
                hard_game.setVisibility(View.VISIBLE);
                hardOne.startAnimation(floating);
                hardTwo.startAnimation(floating);
                hardThree.startAnimation(floating);
                hardFour.startAnimation(floating);
                hardFive.startAnimation(floating);
                break;
        }

        countDownTimer = new MyCountDownTimer(totalTime , 500);
        countDownTimer.start();

        changeColors();

        view.findViewById(R.id.bubble).startAnimation(floating2);
        view.findViewById(R.id.bubble2).startAnimation(floating2);
        view.findViewById(R.id.bubble3).startAnimation(floating2);
        view.findViewById(R.id.bubble5).startAnimation(floating2);
        freeze.startAnimation(floating);
        return view;
    }

    private void gameOver() {
        stopGame();
        progressBar.setAnimationDuration(1000);
        progressBar.setProgress(0);
        wrong.start();
        game_over_dialog.setAnimation(AppConstants.dialogEnter(300,400));
        game_over_dialog.setVisibility(View.VISIBLE);
        dialog_score.setText("" + score);
        ans.setText(answer);
        switch (level) {
            case 0:
                Games.Leaderboards.submitScore(((MainActivity) getActivity()).mGoogleApiClient, getString(R.string.leaderboard_easy), score);

                if (score >= 10) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_easy_10));
                }
                if (score >= 20) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_easy_20));
                }
                if (score >= 50) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_easy_50));
                }
                if (score >= 80) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_easy_80));
                }
                if (score >= 100) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_easy_100));
                }
                break;
            case 1:

                Games.Leaderboards.submitScore(((MainActivity) getActivity()).mGoogleApiClient, getString(R.string.leaderboard_medium), score);

                if (score >= 10) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_medium_10));
                }
                if (score >= 20) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_medium_20));
                }
                if (score >= 50) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_medium_50));
                }
                if (score >= 80) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_medium_80));
                }
                if (score >= 100) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_medium_100));
                }
                break;
            case 2:

                Games.Leaderboards.submitScore(((MainActivity) getActivity()).mGoogleApiClient, getString(R.string.leaderboard_hard), score);

                if (score >= 10) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_hard_10));
                }
                if (score >= 20) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_hard_20));
                }
                if (score >= 50) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_hard_50));
                }
                if (score >= 80) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_hard_80));
                }
                if (score >= 100) {
                    Games.Achievements
                            .unlock(((MainActivity) getActivity()).mGoogleApiClient,
                                    getString(R.string.achievement_hard_100));
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.easy_one:
                if (answer.equalsIgnoreCase(easyOne.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    gameOver();
                }
                break;

            case R.id.easy_two:
                if (answer.equalsIgnoreCase(easyTwo.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    gameOver();
                }
                break;

            case R.id.easy_three:
                if (answer.equalsIgnoreCase(easyThree.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    gameOver();
                }
                break;

            case R.id.medium_one:
                if (answer.equalsIgnoreCase(mediumOne.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    gameOver();
                }
                break;

            case R.id.medium_two:
                if (answer.equalsIgnoreCase(mediumTwo.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    gameOver();
                }
                break;

            case R.id.medium_three:
                if (answer.equalsIgnoreCase(mediumThree.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    gameOver();
                }
                break;

            case R.id.medium_four:
                if (answer.equalsIgnoreCase(mediumFour.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    gameOver();
                }
                break;

            case R.id.hard_one:
                if (answer.equalsIgnoreCase(hardOne.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    gameOver();
                }
                break;

            case R.id.hard_two:
                if (answer.equalsIgnoreCase(hardTwo.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    gameOver();
                }
                break;

            case R.id.hard_three:
                if (answer.equalsIgnoreCase(hardThree.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    gameOver();
                }
                break;

            case R.id.hard_four:
                if (answer.equalsIgnoreCase(hardFour.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    gameOver();
                }
                break;

            case R.id.hard_five:
                if (answer.equalsIgnoreCase(hardFive.getText().toString())) {
                    view.startAnimation(AppConstants.getBubbleAnimation(getContext(), view));
                    onCorrectAnswer();
                } else {
                    gameOver();
                }
                break;

            case R.id.home:
                closeGameFragment.closeFragment();
                break;

            case R.id.time_freezer:
                if (!stopped) {
                    if (powerups>0) {
                        pauseResumeTimer();
                        freeze.setText(getString(R.string.time_freezers)+(--powerups));
                    }else {
                        MyToast.showShort(getContext(), getString(R.string.no_freezers_left));
                    }

                } else {
                    MyToast.showShort(getContext(), getString(R.string.already_paused));
                }
                break;

            case R.id.high_score:
                viewLeaderboard();
                break;

            case R.id.try_again:
                tryAgain();
                break;

            case R.id.share:
                share_score();
                break;

            case R.id.close_dialog:
                closeGameFragment.closeFragment();
        }
    }

    void pauseResumeTimer() {
        MyToast.showShort(getContext(), getString(R.string.timer_paused));
        countDownTimer.cancel();
        stopped = true;
        progressBar.setAnimationDuration(0);
        progressBar.setProgress(0);
        handler.postDelayed(runnable, 5000);
    }

    private void changeColors() {
        ArrayList<Integer> colorslist = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            colorslist.add(AppConstants.splashBackground[i]);
        }
        Collections.shuffle(colorslist);
        switch (level) {
            case 0:

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
                break;

            case 1:
                ArrayList<String> mediumNamelist = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    mediumNamelist.add(AppConstants.mediumColorNames[i]);
                }
                Collections.shuffle(mediumNamelist);

                mediumOne.setBackgroundResource(colorslist.get(0));
                mediumTwo.setBackgroundResource(colorslist.get(1));
                mediumThree.setBackgroundResource(colorslist.get(2));
                mediumFour.setBackgroundResource(colorslist.get(3));

                mediumOne.setText(mediumNamelist.get(0));
                mediumTwo.setText(mediumNamelist.get(1));
                mediumThree.setText(mediumNamelist.get(2));
                mediumFour.setText(mediumNamelist.get(3));

                Random random1 = new Random();
                answer = mediumNamelist.get(random1.nextInt(4));
                ansPos = Arrays.asList(AppConstants.mediumColorNames).indexOf(answer);

                int colorFrom1 = Color.TRANSPARENT;
                Drawable back1 = background.getBackground();
                if (back1 instanceof ColorDrawable)
                    colorFrom1 = ((ColorDrawable) back1).getColor();
                int colorTo1 = getResources().getColor(AppConstants.mediumColorCodes[ansPos]);
                ValueAnimator colorAnimation1 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom1, colorTo1);
                colorAnimation1.setDuration(400); // milliseconds
                colorAnimation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        background.setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation1.start();
                break;

            case 2:
                ArrayList<String> hardNamelist = new ArrayList<>();
                for (int i = 0; i < 15; i++) {
                    hardNamelist.add(AppConstants.hardColorNames[i]);
                }
                Collections.shuffle(hardNamelist);

                hardOne.setBackgroundResource(colorslist.get(0));
                hardTwo.setBackgroundResource(colorslist.get(1));
                hardThree.setBackgroundResource(colorslist.get(2));
                hardFour.setBackgroundResource(colorslist.get(3));
                hardFive.setBackgroundResource(colorslist.get(4));

                hardOne.setText(hardNamelist.get(0));
                hardTwo.setText(hardNamelist.get(1));
                hardThree.setText(hardNamelist.get(2));
                hardFour.setText(hardNamelist.get(3));
                hardFive.setText(hardNamelist.get(4));

                Random random2 = new Random();
                answer = hardNamelist.get(random2.nextInt(5));
                ansPos = Arrays.asList(AppConstants.hardColorNames).indexOf(answer);

                int colorFrom2 = Color.TRANSPARENT;
                Drawable back2 = background.getBackground();
                if (back2 instanceof ColorDrawable)
                    colorFrom2 = ((ColorDrawable) back2).getColor();
                int colorTo2 = getResources().getColor(AppConstants.hardColorCodes[ansPos]);
                ValueAnimator colorAnimation2 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom2, colorTo2);
                colorAnimation2.setDuration(400); // milliseconds
                colorAnimation2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        background.setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation2.start();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopGame();
    }

    void stopGame() {
        if (stopped) {
            handler.removeCallbacksAndMessages(null);
        }
        countDownTimer.cancel();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countDownTimer = new MyCountDownTimer(currentTimeLeft, 500);
            countDownTimer.start();
            progressBar.setAnimationDuration((int)currentTimeLeft);
            progressBar.setProgress(1);

            stopped = false;
        }
    };

    private void onCorrectAnswer() {
        score++;
        if (score == 15 || score == 30 || score == 45 || score == 60 || score == 75|| score == 90|| score == 105) {
            totalTime=totalTime-500;
            MyToast.showShort(getContext(), getString(R.string.timer_reduced)+" "+(float)totalTime/1000);
        }
        Score.setText("" + score);
        countDownTimer.cancel();
        if (!stopped) {
            countDownTimer = new MyCountDownTimer(totalTime,    500);
            countDownTimer.start();
            progressBar.setAnimationDuration(0);
            progressBar.setProgress(0);
            progressBar.setAnimationDuration(totalTime);
            progressBar.setProgress(1);
        }
        changeColors();
        correct.start();

    }

    private void checkPayment() {
        if (AppConstants.paid1) {
            powerups=4;
            freeze.setText(getString(R.string.time_freezers_4));
        }
        if (AppConstants.paid2) {
            powerups=5;
            freeze.setText(getString(R.string.time_freezers_5));
        }
        if (AppConstants.paid4) {
            powerups=6;
            freeze.setText(getString(R.string.time_freezers_6));
        }

    }

    public void viewLeaderboard() {
        switch (level) {
            case 0:
                startActivityForResult(
                        Games.Leaderboards.getLeaderboardIntent(((MainActivity) getActivity()).mGoogleApiClient,
                                getString(R.string.leaderboard_easy)), 0);
                break;
            case 1:
                startActivityForResult(
                        Games.Leaderboards.getLeaderboardIntent(((MainActivity) getActivity()).mGoogleApiClient,
                                getString(R.string.leaderboard_medium)), 0);
                break;
            case 2:
                startActivityForResult(
                        Games.Leaderboards.getLeaderboardIntent(((MainActivity) getActivity()).mGoogleApiClient,
                                getString(R.string.leaderboard_hard)), 0);
                break;
        }
    }

    public void tryAgain() {
        game_over_dialog.setVisibility(View.GONE);
        totalTime=5000;
        score=0;
        stopped=false;
        Score.setText("0");
        powerups = 3;
        freeze.setText(getString(R.string.time_freezers_3));
        progressBar.setAnimationDuration(0);
        progressBar.setProgress(0);
        progressBar.setAnimationDuration(totalTime);
        progressBar.setProgress(1);
        checkPayment();
        changeColors();
    }

    public void share_score() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, "Can you beat my score of " + score + " on Taplor?\n\nDownload the app now-\nhttps://play.google.com/store/apps/details?id=com.riseapps.taplor");

        startActivity(Intent.createChooser(share, "Share Score!"));
    }

    private class MyCountDownTimer extends CountDownTimer {
        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long l) {
            currentTimeLeft = l;
        }

        @Override
        public void onFinish() {
            gameOver();
        }
    }

}
