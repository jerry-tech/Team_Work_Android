package com.teamwork.teamwork;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ResponseDialog {

    public void showCancelableDialog(String title, String message, int drawable, Context context, Drawable background){

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context,R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog);
        builder.setTitle(title);
        builder.setIcon(drawable);
        builder.setMessage(message);
        builder.setBackground(background);
        builder.show();
//        getResources().getDrawable(R.drawable.alert_bg,null)
    }
}
