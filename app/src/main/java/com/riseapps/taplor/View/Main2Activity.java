package com.riseapps.taplor.View;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riseapps.taplor.Executor.RecyclerViewAdapter;
import com.riseapps.taplor.Model.Player;
import com.riseapps.taplor.R;
import com.riseapps.taplor.Utils.AppConstants;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    ArrayList<Player> easyPlayers=new ArrayList<>();
    ArrayList<Player> mediumPlayers=new ArrayList<>();
    ArrayList<Player> hardPlayers=new ArrayList<>();
    RecyclerView easyView,mediumView,hardView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        onWindowFocusChanged(true);
        easyView=findViewById(R.id.easy_recycler);
        mediumView=findViewById(R.id.medium_recycler);
        hardView=findViewById(R.id.hard_recycler);
        easyView.setHasFixedSize(true);
        easyView.setNestedScrollingEnabled(false);
        mediumView.setHasFixedSize(true);
        mediumView.setNestedScrollingEnabled(false);
        hardView.setHasFixedSize(true);
        hardView.setNestedScrollingEnabled(false);
        mAdView = findViewById(R.id.adView);
        if (AppConstants.paid3 || AppConstants.paid4) {
            mAdView.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("1BB6AD3C4E832E63122601E2E4752AF4")
                    .build();
            mAdView.loadAd(adRequest);
        }


        easyView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mediumView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        hardView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Easy Players");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Player player=ds.getValue(Player.class);
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
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Player player=ds.getValue(Player.class);
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
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Player player=ds.getValue(Player.class);
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

}
