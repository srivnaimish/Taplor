package com.riseapps.taplor.View;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.riseapps.taplor.Executor.CloseGameFragment;
import com.riseapps.taplor.R;
import com.riseapps.taplor.Utils.AppConstants;
import com.riseapps.taplor.Utils.SharedPreferenceSingelton;
import com.riseapps.taplor.Widgets.MyToast;
import com.riseapps.taplor.billing.IabHelper;
import com.riseapps.taplor.billing.IabResult;
import com.riseapps.taplor.billing.Inventory;
import com.riseapps.taplor.billing.Purchase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, CloseGameFragment {

    private static final String TAG = "In-App Billing";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    ConstraintLayout background;
    TextView heading;
    Button easy, medium, hard;
    ImageButton rank, achievement, colors, purchase, about;
    //TODO:ADS
    private AdView mAdView;
    CardView premium, about_game, game_colors;

    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false;
    boolean dialogOpen = false;

    public static int RC_SIGN_IN = 9001;
    private static final int REQUEST_LEADERBOARD = 101;

    public boolean mResolvingConnectionFailure = false;
    public boolean mAutoStartSignInFlow = true;
    public boolean mSignInClicked = false;
    int ansPos = 0;

    public GoogleApiClient mGoogleApiClient;

    private int colorFrom;
    private int colorTo1;
    SharedPreferenceSingelton sharedPreferenceSingelton = new SharedPreferenceSingelton();
    IabHelper mHelper;
    private boolean billinSupported;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onWindowFocusChanged(true);
        setContentView(R.layout.activity_main);

        background = findViewById(R.id.container);
        heading = findViewById(R.id.textView);
        easy = findViewById(R.id.easy);
        medium = findViewById(R.id.medium);
        hard = findViewById(R.id.hard);
        rank = findViewById(R.id.rank);
        achievement = findViewById(R.id.achievement);
        purchase = findViewById(R.id.purchase);
        about = findViewById(R.id.about);
        colors = findViewById(R.id.colors);

        premium = findViewById(R.id.premium_dialog);
        about_game = findViewById(R.id.about_game);
        game_colors = findViewById(R.id.game_colors);

        mAdView = findViewById(R.id.adView);
        /*if(!sharedPreferenceSingelton.getSavedBoolean(this,"Payment")) {
            AdRequest adRequest = new AdRequest.Builder()
                    //.addTestDevice("1BB6AD3C4E832E63122601E2E4752AF4")
                    .build();
            mAdView.loadAd(adRequest);
        }else {
            mAdView.setVisibility(View.GONE);
        }*/

        mHelper = new IabHelper(this, AppConstants.KEY);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app billing failed " + result);
                    billinSupported = false;
                } else {
                    Log.d(TAG, "In-app billing setup OK ");
                    billinSupported = true;
                    if (mHelper == null) return;
                    List<String> st = new ArrayList<String>();
                    st.add(AppConstants.products);
                    try {
                        mHelper.queryInventoryAsync(true, st, mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();


        heading.setAnimation(AppConstants.generateFadeInAnimator(1000, 2000));
        easy.setAnimation(AppConstants.generateFadeInAnimator(0, 2000));
        medium.setAnimation(AppConstants.generateFadeInAnimator(0, 3000));
        hard.setAnimation(AppConstants.generateFadeInAnimator(0, 4000));

        rank.setAnimation(AppConstants.generateBottomUpFadeInAnimator());
        achievement.setAnimation(AppConstants.generateBottomUpFadeInAnimator());
        purchase.setAnimation(AppConstants.generateBottomUpFadeInAnimator());
        about.setAnimation(AppConstants.generateBottomUpFadeInAnimator());
        colors.setAnimation(AppConstants.generateBottomUpFadeInAnimator());

        colorFrom = getResources().getColor(R.color.ONE);
        Drawable back = background.getBackground();
        if (back instanceof ColorDrawable)
            colorFrom = ((ColorDrawable) back).getColor();
    }

    public void launchEasyLevel(View v) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            GameFragment gamesFragment = new GameFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("LEVEL", 0);
            gamesFragment.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
            ft.replace(R.id.container, gamesFragment);
            ft.addToBackStack("GamesFragment");
            ft.commit();
        }
    }

    public void launchMediumLevel(View v) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            GameFragment gamesFragment = new GameFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("LEVEL", 1);
            gamesFragment.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
            ft.replace(R.id.container, gamesFragment);
            ft.addToBackStack("GamesFragment");
            ft.commit();
        }
    }

    public void launchHardLevel(View v) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            GameFragment gamesFragment = new GameFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("LEVEL", 2);
            gamesFragment.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
            ft.replace(R.id.container, gamesFragment);
            ft.addToBackStack("GamesFragment");
            ft.commit();
        }
    }

    public Runnable mProgressRunner = new Runnable() {
        @Override
        public void run() {
            changeBackgroundColor();
            background.postDelayed(mProgressRunner, 4000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mProgressRunner.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        background.removeCallbacks(mProgressRunner);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, R.string.signin_other_error)) {
                mResolvingConnectionFailure = false;
            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mInSignInFlow && !mExplicitSignOut) {
            // auto sign in
            mGoogleApiClient.connect();
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.signin_failure);
            }
        }

    }

    @Override
    public void closeFragment() {
        getSupportFragmentManager().popBackStackImmediate();
    }


    public void changeBackgroundColor() {
        colorTo1 = getResources().getColor(AppConstants.GradientPrimaryColors[ansPos]);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo1);
        colorAnimation.setDuration(2500); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                GradientDrawable drawable = new GradientDrawable(
                        GradientDrawable.Orientation.BR_TL, new int[]{(int) animator.getAnimatedValue(), R.color.SIX});
                background.setBackground(drawable);
                colorFrom = colorTo1;
            }

        });
        colorAnimation.start();
        ansPos++;
        ansPos = ansPos % 5;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (billinSupported) {
            if (mHelper != null) try {
                mHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
            mHelper = null;
        }
    }

    public void openRanking(View view) {
        startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient), REQUEST_LEADERBOARD);
    }

    public void openAchievement(View view) {
        startActivityForResult(
                Games.Achievements
                        .getAchievementsIntent(mGoogleApiClient), REQUEST_LEADERBOARD
        );
    }

    public void openPremiumDialog(View view) {
        if (!dialogOpen) {
            premium.setAnimation(AppConstants.dialogEnter());
            premium.setVisibility(View.VISIBLE);
            dialogOpen = true;
        }
    }

    public void closePremiumDialog(View view) {
        if (dialogOpen) {
            AnimationSet animationSet = AppConstants.dialogExit();
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    premium.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            premium.setAnimation(animationSet);
            dialogOpen = false;
        }
    }

    public void paymentStart(View view) {
        if (billinSupported) {
            try {
                mHelper.launchPurchaseFlow(MainActivity.this, AppConstants.products, 10001, mPurchaseFinishedListener, "mypurchaseToken");
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(MainActivity.this, "Billing Not Supported on Your Device", Toast.LENGTH_SHORT).show();
    }

    public void openExportDialog(View view) {
        if (!dialogOpen) {
            about_game.setAnimation(AppConstants.dialogEnter());
            about_game.setVisibility(View.VISIBLE);
            dialogOpen = true;
        }
    }

    public void closeExportDialog(View view) {
        if (dialogOpen) {
            AnimationSet animationSet = AppConstants.dialogExit();
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    about_game.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            about_game.setAnimation(animationSet);
            dialogOpen = false;
        }

    }


    public void shareApp(View view) {
        String message = "Checkout this awesome game, Taplor on Play Store\n\n https://play.google.com/store/apps/details?id=com.riseapps.taplor";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(share, "Share via.."));
    }

    public void reviewApp(View view) {
        final Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);
        if (getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0) {
            startActivity(rateAppIntent);
        }
    }

    public void seeCredits(View view) {
        Toast.makeText(this, "See Credits", Toast.LENGTH_SHORT).show(); //TODO :CREDITS
    }

    public void launchFBPage(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.FB_URL));
        startActivity(browserIntent);
    }   //TODO:Replace URL with correct

    public void launchTwitterPage(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.TWITTER_URL));
        startActivity(browserIntent);
    }   //TODO:Replace URL with correct

    public void launchTutorial(View view) {
        DemoFragment demoFragment = new DemoFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.container, demoFragment);
        ft.addToBackStack("DemoFragment");
        ft.commit();
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            if (result.isFailure()) {
                MyToast.showShort(MainActivity.this, getString(R.string.aborted));
                return;
            }
            if (info.getSku().equalsIgnoreCase(AppConstants.products)) {
                sharedPreferenceSingelton.saveAs(MainActivity.this, "Payment", true);
                MyToast.showShort(MainActivity.this, getString(R.string.thanks));
                mAdView.setVisibility(View.GONE);
            }
        }
    };

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            if (mHelper == null) return;

            if (result.isFailure()) {
                return;
            }

            if (inventory.hasPurchase(AppConstants.products)) {
                new SharedPreferenceSingelton().saveAs(MainActivity.this, "Payment", true);
            }
        }
    };


    public void openColors(View view) {
        if (!dialogOpen) {
            game_colors.setAnimation(AppConstants.dialogEnter());
            game_colors.setVisibility(View.VISIBLE);
            dialogOpen = true;
        }
    }

    public void closeColorsDialog(View view) {
        if (dialogOpen) {
            AnimationSet animationSet = AppConstants.dialogExit();
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    game_colors.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            game_colors.setAnimation(animationSet);
            dialogOpen = false;
        }

    }
}
