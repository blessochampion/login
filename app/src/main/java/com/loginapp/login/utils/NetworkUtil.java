package com.loginapp.login.utils;

/**
 * Created by Blessing.Ekundayo on 9/5/2017.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;


public class NetworkUtil {
    private static final String TAG = NetworkUtil.class.getSimpleName();

    private static final String BASE_URL = "https://loginappng.herokuapp.com/api/users/";
    private static final String LOGIN = "authenticate";
    public static final int FAILURE_RESPONSE_CODE = 0;
    public static final int SUCCESS_RESPONSE_CODE = 1;


    public static String getLoginUrl() {
        Uri loginUri = Uri.parse(BASE_URL).buildUpon().appendPath(LOGIN)
                .build();

        return loginUri.toString();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }






}
