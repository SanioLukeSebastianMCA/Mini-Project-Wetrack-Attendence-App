package com.sanioluke00.wetrack.DataModels;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.sanioluke00.wetrack.R;

import org.jetbrains.annotations.NotNull;

//@SuppressWarnings("all")
public class Functions {

    public void checkTheme(@NotNull Context context) {

        SharedPreferences prefs = context.getSharedPreferences("get_all_data", MODE_PRIVATE);
        int isCheck = prefs.getInt("get_theme", 0);

        if (isCheck == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (isCheck == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (isCheck == 2) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void whiteStatusBarDesign(@NotNull Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
    }

    public void darkStatusBarDesign(@NotNull Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.black));
    }

    public void lightstatusbardesign(@NotNull Activity activity) {

        SharedPreferences prefs_2 = activity.getSharedPreferences("get_all_data", MODE_PRIVATE);
        int isCheck = prefs_2.getInt("get_theme", 0);

        if (isCheck == 0) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if (isCheck == 1) {
            activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if (isCheck == 2) {

            int nightModeFlags = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    break;

                case Configuration.UI_MODE_NIGHT_NO:
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    break;

                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    break;
            }
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.text_color));
    }

    public void setStatusBarColor(@NotNull Activity activity, int color_id) {
        activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(activity.getResources().getColor(color_id));
    }

    public <Any> void putSharedPrefsValue(@NotNull Context context, String prefs_name, String prefs_objname, @NotNull String type, Any set_val) {

        SharedPreferences.Editor pref_edit = context.getSharedPreferences(prefs_name, MODE_PRIVATE).edit();
        switch (type) {
            case "string":
                pref_edit.putString(prefs_objname, (String) set_val);
                break;

            case "int":
                pref_edit.putInt(prefs_objname, (Integer) set_val);
                break;

            case "boolean":
                pref_edit.putBoolean(prefs_objname, (Boolean) set_val);
                break;

            case "float":
                pref_edit.putFloat(prefs_objname, (Float) set_val);
                break;

            case "long":
                pref_edit.putFloat(prefs_objname, (Long) set_val);
                break;
        }
        pref_edit.apply();
    }

    public <Any> Any getSharedPrefsValue(@NotNull Context context, String prefs_name, String prefs_objname, @NotNull String type, Any default_val) {

        SharedPreferences pref = context.getSharedPreferences(prefs_name, MODE_PRIVATE);
        switch (type) {

            case "string":
                String stringval = pref.getString(prefs_objname, (String) default_val);
                return ((Any) (String) stringval);

            case "int":
                int intval = pref.getInt(prefs_objname, (Integer) default_val);
                return ((Any) (Integer) intval);

            case "boolean":
                Boolean boolval = pref.getBoolean(prefs_objname, (Boolean) default_val);
                return ((Any) (Boolean) boolval);

            case "float":
                Float floatval = pref.getFloat(prefs_objname, (Float) default_val);
                return ((Any) (Float) floatval);

            case "long":
                Long longval = pref.getLong(prefs_objname, (Long) default_val);
                return ((Any) (Long) longval);

            default:
                return null;
        }
    }

    public Dialog createDialogBox(Activity activity, int view_id, boolean isclose) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(view_id);
        dialog.setCanceledOnTouchOutside(isclose);
        dialog.setCancelable(isclose);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        return dialog;
    }


}
