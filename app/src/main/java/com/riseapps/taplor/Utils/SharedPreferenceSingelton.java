package com.riseapps.taplor.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by naimish on 12/3/17.
 */

public class SharedPreferenceSingelton {

    public void saveAs(Context c, String name, boolean value) {
        SharedPreferences sp = c.getSharedPreferences("player", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public boolean getSavedBoolean(Context c, String name) {
        SharedPreferences sp = c.getSharedPreferences("player", MODE_PRIVATE);
        return sp.getBoolean(name, true);
    }



}
