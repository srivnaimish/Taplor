package com.riseapps.taplor.Widgets;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.riseapps.taplor.R;

/**
 * Created by naimish on 15/9/17.
 */

public class MyToast {
    private static Toast currentToast;

    public static void showShort(Context context, String message){
        if(currentToast != null) currentToast.cancel();
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) ((Activity) context).findViewById(R.id.root));
        TextView text = layout.findViewById(R.id.message);

        text.setText(message);

        Toast toast = new Toast(context);

        toast.setDuration(Toast.LENGTH_SHORT);

        toast.setView(layout);
        toast.show();
        currentToast = toast;
    }

    public static void showLong(Context context, String message){
        if(currentToast != null) currentToast.cancel();
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) ((Activity) context).findViewById(R.id.root));
        TextView text = layout.findViewById(R.id.message);

        text.setText(message);

        Toast toast = new Toast(context);

        toast.setDuration(Toast.LENGTH_LONG);

        toast.setView(layout);
        toast.show();
        currentToast = toast;
    }
}
