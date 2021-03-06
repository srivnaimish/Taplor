package com.riseapps.taplor.View;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riseapps.taplor.Executor.RecyclerViewAdapter;
import com.riseapps.taplor.Model.Player;
import com.riseapps.taplor.R;
import com.riseapps.taplor.Utils.AppConstants;
import com.riseapps.taplor.Widgets.MyToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    ArrayList<Player> easyPlayers = new ArrayList<>();
    ArrayList<Player> mediumPlayers = new ArrayList<>();
    ArrayList<Player> hardPlayers = new ArrayList<>();
    RecyclerView easyView, mediumView, hardView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private AdView mAdView;
    private ConstraintLayout background;
    private NestedScrollView nestedScrollView;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        onWindowFocusChanged(true);
        easyView = findViewById(R.id.easy_recycler);
        mediumView = findViewById(R.id.medium_recycler);
        hardView = findViewById(R.id.hard_recycler);
        easyView.setHasFixedSize(true);
        easyView.setNestedScrollingEnabled(false);
        mediumView.setHasFixedSize(true);
        mediumView.setNestedScrollingEnabled(false);
        hardView.setHasFixedSize(true);
        hardView.setNestedScrollingEnabled(false);
        mAdView = findViewById(R.id.adView);

        background = findViewById(R.id.background);
        nestedScrollView = findViewById(R.id.nested);
        avLoadingIndicatorView = findViewById(R.id.loader);
        avLoadingIndicatorView.smoothToShow();

        easyView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mediumView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        hardView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Easy Players");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Player player = ds.getValue(Player.class);
                    easyPlayers.add(player);
                }
                recyclerViewAdapter = new RecyclerViewAdapter(Main2Activity.this, easyView, easyPlayers);
                easyView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference("Medium");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Player player = ds.getValue(Player.class);
                    mediumPlayers.add(player);
                }
                recyclerViewAdapter = new RecyclerViewAdapter(Main2Activity.this, mediumView, mediumPlayers);
                mediumView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("Hard");
        try {
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Player player = ds.getValue(Player.class);
                        hardPlayers.add(player);
                    }
                    recyclerViewAdapter = new RecyclerViewAdapter(Main2Activity.this, hardView, hardPlayers);
                    hardView.setAdapter(recyclerViewAdapter);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    //Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }catch (Exception e){
            MyToast.showShort(this,e.getMessage());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int colorFrom= Color.parseColor("#D32F2F");
                int colorTo=Color.parseColor("#000000");
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(500); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                background.setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();
                avLoadingIndicatorView.smoothToHide();
                nestedScrollView.setAnimation(AppConstants.generateFadeInAnimator(0, 2000));
                nestedScrollView.setVisibility(View.VISIBLE);
            }
        }, 3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppConstants.paid3 || AppConstants.paid4) {
                } else {
                    mInterstitialAd = new InterstitialAd(Main2Activity.this);
                    mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_id));
                    AdRequest adRequest = new AdRequest.Builder()
                            .build();
                    mInterstitialAd.loadAd(adRequest);
                    mInterstitialAd.setAdListener(new AdListener(){
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            mInterstitialAd.show();
                        }
                    });
                }

            }
        }, 4000);


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

}
