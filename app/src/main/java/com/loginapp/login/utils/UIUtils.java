package com.loginapp.login.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import static android.R.attr.duration;

/**
 * Created by Blessing.Ekundayo on 8/31/2017.
 */

public class UIUtils
{
    public static void hideKeyboard(Activity activity) {

        View view = activity.getCurrentFocus();

        if (view != null) {

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }

    }

    public static void showToast(Activity activity , String message){
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}
