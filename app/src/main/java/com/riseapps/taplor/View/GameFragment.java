package com.riseapps.taplor.View;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class GameFragment extends Fragment implements View.OnClickListener {
    private static final String LEVEL = "LEVEL";

    LinearLayout easy_game, medium_game, hard_game;
    ConstraintLayout background;
    Button easyOne, easyTwo, easyThree;
    Button mediumOne, mediumTwo, mediumThree, mediumFour;
    Button hardOne, hardTwo, hardThree, hardFour, hardFive;
    String answer = "";
    Button timer;
    int score = 0;
    int totalTime = 6;
    SharedPreferenceSingelton sharedPreferenceSingelton = new SharedPreferenceSingelton();
    CloseGameFragment closeGameFragment;
    long currentTimeLeft;
    private int level,powerups=3;
    private CountDownTimer countDownTimer;
    private int ansPos;
    private Button freeze;
    boolean stopped = false;
    private Dialog dialog;
    private long timeToShow;
    private MediaPlayer correct, wrong;
    private TextView Score;
    private Handler handler = new Handler();
    private WaveHelper mWaveHelper;

    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 0;


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

        freeze = view.findViewById(R.id.time_freezer);

        WaveView waveView = view.findViewById(R.id.wave);
        waveView.setBorder(mBorderWidth, mBorderColor);

        mWaveHelper = new WaveHelper(waveView);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        waveView.setWaveColor(
                Color.parseColor("#0DFAFAFA"),
                Color.parseColor("#0DFAFAFA"));
        mWaveHelper.start();

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

        Animation floating = AppConstants.getFloatingAnimation(getContext());

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

        timer = view.findViewById(R.id.timer);

        countDownTimer = new MyCountDownTimer(totalTime * 1000, 1000);
        countDownTimer.start();
        changeColors();

        correct = MediaPlayer.create(getContext(), R.raw.correct);
        wrong = MediaPlayer.create(getContext(), R.raw.wrong);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void gameOver() {
        stopGame();
        wrong.start();
        timer.setVisibility(View.GONE);
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.game_over);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        try {
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        } catch (NullPointerException e) {
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        TextView Score = dialog.findViewById(R.id.score);
        Button leaderboard = dialog.findViewById(R.id.high_score);
        Score.setText("" + score);

        switch (level) {
            case 0:
                Games.Leaderboards.submitScore(((MainActivity) getActivity()).mGoogleApiClient, getString(R.string.leaderboard_easy), score);
                leaderboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(
                                Games.Leaderboards.getLeaderboardIntent(((MainActivity) getActivity()).mGoogleApiClient,
                                        getString(R.string.leaderboard_easy)), 0);
                    }
                });
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
                leaderboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(
                                Games.Leaderboards.getLeaderboardIntent(((MainActivity) getActivity()).mGoogleApiClient,
                                        getString(R.string.leaderboard_medium)), 0);
                    }
                });
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
                leaderboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivityForResult(
                                Games.Leaderboards.getLeaderboardIntent(((MainActivity) getActivity()).mGoogleApiClient,
                                        getString(R.string.leaderboard_hard)), 0);
                    }
                });
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

        Button tryAgain = dialog.findViewById(R.id.try_again);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                closeGameFragment.restartGame(level);
            }
        });
        Button share = dialog.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTextUrl();
            }
        });
        dialog.show();

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    closeGameFragment.closeGame();
                }
                return true;
            }
        });
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

        }
    }

    void pauseResumeTimer() {
        MyToast.showShort(getContext(), getString(R.string.timer_paused));
        countDownTimer.cancel();
        stopped = true;
        handler.postDelayed(runnable, 5000);
    }

    private void changeColors() {
        countDownTimer.cancel();
        if (!stopped) {
            countDownTimer = new MyCountDownTimer(totalTime * 1000, 1000);
            countDownTimer.start();
        }
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setNavigationBarColor(getResources().getColor(AppConstants.easyColorCodes[ansPos]));
                }

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setNavigationBarColor(getResources().getColor(AppConstants.mediumColorCodes[ansPos]));
                }
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setNavigationBarColor(getResources().getColor(AppConstants.hardColorCodes[ansPos]));
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (stopped) {
            handler.removeCallbacksAndMessages(null);
        }
        stopGame();

    }

    void stopGame() {
        timer.removeCallbacks(runnable);
        countDownTimer.cancel();
        timer.setVisibility(View.INVISIBLE);
        mWaveHelper.cancel();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countDownTimer = new MyCountDownTimer(currentTimeLeft, 1000);
            countDownTimer.start();
            stopped = false;
        }
    };


    private void onCorrectAnswer() {
        score++;
        if (score == 20 || score == 50 || score == 80 || score == 120) {
            totalTime--;
            MyToast.showShort(getContext(), getString(R.string.timer_reduced));
        }
        Score.setText("" + score);
        changeColors();
        correct.start();
    }

    private class MyCountDownTimer extends CountDownTimer {
        MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long l) {
            currentTimeLeft = l;
            timeToShow = currentTimeLeft / 1000;
            timer.setText("" + timeToShow);
        }

        @Override
        public void onFinish() {
            gameOver();
        }
    }

    private void shareTextUrl() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, "Can you beat my score of " + score + " on ColorMind?\n\nDownload the app now-\nhttps://play.google.com/store/apps/details?id=com.riseapps.taplor");

        startActivity(Intent.createChooser(share, "Share Score!"));
    }

    private void checkPayment() {
        if (sharedPreferenceSingelton.getSavedBoolean(getContext(), "Payment")) {
            powerups=5;
        }
    }
}
