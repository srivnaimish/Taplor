package com.riseapps.taplor.Model;

/**
 * Created by naimish on 26/9/17.
 */

public class Player {
    private String Name;
    private long Score;


    public Player(){}

    public Player(String Name, long Score) {
        this.Name = Name;
        this.Score = Score;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public long getScore() {
        return Score;
    }

    public void setScore(long Score) {
        this.Score = Score;
    }
}
