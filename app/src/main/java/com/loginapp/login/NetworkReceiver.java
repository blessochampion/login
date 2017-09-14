package com.loginapp.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loginapp.login.data.UserPreference;
import com.loginapp.login.utils.NetworkUtil;
import com.loginapp.login.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.loginapp.login.HomeActivity.CONTENT;
import static com.loginapp.login.HomeActivity.USER_ID;

/**
 * Created by blessochampion on 9/11/17.
 */

public class NetworkReceiver extends BroadcastReceiver implements Response.Listener<JSONObject>, Response.ErrorListener {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            UserPreference userPreference = UserPreference.getInstance(AppController.getInstance().getApplicationContext());

            if (ni != null && ni.isConnectedOrConnecting() && userPreference.isServeSyncNeeded()) {
                makeNetworkCallForContentUpdate(NetworkUtil.getContentUpdateUrl());
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {

            }
        }
    }

    private void makeNetworkCallForContentUpdate(String url) {

        try {
            JSONObject requestBody = new JSONObject();
            UserPreference preference = UserPreference.getInstance(AppController.getInstance().getApplicationContext());
            requestBody.put(USER_ID, preference.getUserId());
            requestBody.put(CONTENT, "nothing");

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST
                    , url, requestBody, this,  this
            );

            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        } catch (JSONException e) {

        }
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e("ddd", response.toString());
        UserPreference userPreference = UserPreference.getInstance(AppController.getInstance().getApplicationContext());
        userPreference.setServerSyncNeeded(false);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
