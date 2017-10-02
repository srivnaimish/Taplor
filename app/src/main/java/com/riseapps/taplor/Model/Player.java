package com.riseapps.taplor.Model;

import java.io.Serializable;

/**
 * Created by naimish on 26/9/17.
 */

public class Player {
    public String name;
    public long score;


    public Player(){}

    public Player(String Name, long Score) {
        this.name = Name;
        this.score = Score;
    }
}
