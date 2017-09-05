package com.loginapp.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loginapp.login.utils.NetworkUtil;
import com.loginapp.login.utils.PasswordUtil;
import com.loginapp.login.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static android.R.attr.duration;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
    public static final String NO_INTERNET_CONNECTION = "No internet Connection";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    TextView signupTextView;
    TextView loginTextView;
    EditText username;
    EditText password;
    ProgressDialog mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signupTextView = (TextView) findViewById(R.id.tv_sign_up);
        signupTextView.setOnClickListener(this);

        loginTextView = (TextView) findViewById(R.id.login_button);
        loginTextView.setOnClickListener(this);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        mLoadingIndicator = new ProgressDialog(this);
    }


    @Override
    public void onClick(View v) {
        UIUtils.hideKeyboard(this);
        if (v.getId() == R.id.tv_sign_up) {
            signup();
        } else if (v.getId() == R.id.login_button) {


            if (inputIsValid()) {
                if(NetworkUtil.isNetworkAvailable(this)) {
                    makeNetworkCallForLogin(NetworkUtil.getLoginUrl());
                    try {
                        Log.e("password", PasswordUtil.SHA1(password.getText().toString().trim()));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                } else {
                    UIUtils.showToast(this, NO_INTERNET_CONNECTION);
                }
            }

        }
    }



    private void makeNetworkCallForLogin(String url) {

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put(USERNAME, username.getText().toString().trim());
            requestBody.put(PASSWORD, password.getText().toString().trim());

            mLoadingIndicator.setIndeterminate(true);
            mLoadingIndicator.setCanceledOnTouchOutside(false);
            mLoadingIndicator.setMessage("Logging you in... Please wait");
            mLoadingIndicator.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST
                    , url, requestBody, this, this
            );

            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        }catch (JSONException e){

        }
    }

    private boolean inputIsValid() {
        String usernameValue = username.getText().toString();
        String passwordValue = password.getText().toString();
        if(usernameValue.isEmpty()){
            username.setError("Field can not be empty");
            return false;
        }
        if(passwordValue.isEmpty()){
            password.setError("Field can not be empty");
            return false;
        }
        return true;
    }

    private void login() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
    }

    private void signup() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
    }


    @Override
    public void onResponse(JSONObject response) {
        if(mLoadingIndicator != null && mLoadingIndicator.isShowing()){
            mLoadingIndicator.cancel();
        }
        try{
            int code = response.getInt(CODE);
            if(code == NetworkUtil.FAILURE_RESPONSE_CODE){
                UIUtils.showToast(this, response.getString(DESCRIPTION));
            }else {
                login();
            }
        }catch (JSONException e){

        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
