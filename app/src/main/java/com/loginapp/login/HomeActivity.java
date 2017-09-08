package com.loginapp.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loginapp.login.data.UserPreference;
import com.loginapp.login.utils.NetworkUtil;
import com.loginapp.login.utils.PasswordUtil;
import com.loginapp.login.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static com.loginapp.login.LoginFragment.CODE;
import static com.loginapp.login.LoginFragment.DESCRIPTION;
import static com.loginapp.login.LoginFragment.NO_INTERNET_CONNECTION;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {

    public static final String USER_ID = "userId";
    public static final String CONTENT = "content";
    TextView pointTextView;
    TextView addButton;
    EditText contentEditText;
    ProgressDialog mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pointTextView = (TextView) findViewById(R.id.tv_point);
        contentEditText = (EditText) findViewById(R.id.et_contents);
        addButton = (TextView) findViewById(R.id.add_button);
        addButton.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UserPreference userPreference = UserPreference.getInstance(this);
        if (!userPreference.getUsername().isEmpty()) {
            getSupportActionBar().setTitle(userPreference.getUsername());
        }
        if (userPreference.getPoint() >= 0) {
            pointTextView.setText(String.valueOf(userPreference.getPoint()));
        }
        mLoadingIndicator = new ProgressDialog(this);


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_button) {

            UIUtils.hideKeyboard(this);
            if (Integer.valueOf(pointTextView.getText().toString()) < 1) {
                UIUtils.showToast(this, "No more points");
                return;
            }
            if (inputIsValid()) {
                if (NetworkUtil.isNetworkAvailable(this)) {
                    makeNetworkCallForContentUpdate(NetworkUtil.getContentUpdateUrl());

                } else {
                    UIUtils.showToast(this, NO_INTERNET_CONNECTION);
                }
            } else {
                contentEditText.setError("Field Can not be empty");
            }
        }
    }

    private boolean inputIsValid() {
        return !contentEditText.getText().toString().isEmpty();
    }

    private void makeNetworkCallForContentUpdate(String url) {

        try {
            JSONObject requestBody = new JSONObject();
            UserPreference preference = UserPreference.getInstance(this);
            requestBody.put(USER_ID, preference.getUserId());
            requestBody.put(CONTENT, contentEditText.getText().toString().trim());

            mLoadingIndicator.setIndeterminate(true);
            mLoadingIndicator.setCanceledOnTouchOutside(false);
            mLoadingIndicator.setMessage("Saving your content");
            mLoadingIndicator.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST
                    , url, requestBody, this, this
            );

            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        } catch (JSONException e) {

        }
    }

    private void deductPoint() {
        int currentValue = Integer.valueOf(pointTextView.getText().toString());
        if (currentValue > 0) {
            currentValue--;
            pointTextView.setText(String.valueOf(currentValue));
        } else {
            contentEditText.setError("You do not have enough points");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            UserPreference.getInstance(this).setUserLoggedIn(false);
            Intent intent = new Intent(HomeActivity.this, AuthenticationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity(intent);
            overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        UIUtils.showToast(this, "Something went wrong, please try again...");
    }

    @Override
    public void onResponse(JSONObject response) {
        if (mLoadingIndicator != null && mLoadingIndicator.isShowing()) {
            mLoadingIndicator.cancel();
        }
        try {
            int code = response.getInt(CODE);
            if (code == NetworkUtil.FAILURE_RESPONSE_CODE) {
                UIUtils.showToast(this, response.getString(DESCRIPTION));
            } else {
                UIUtils.showToast(this, "Added successfully");
                deductPoint();
            }
        } catch (JSONException e) {

        }
    }
}
