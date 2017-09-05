package com.loginapp.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    public static final String EMAIL = "email";
    TextView loginTextView;
    TextView signupTextView;
    EditText usernameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String NO_INTERNET_CONNECTION = "No internet Connection";
    private String email;
    private String username;
    private String password;
    ProgressDialog mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginTextView = (TextView) findViewById(R.id.tv_login);
        loginTextView.setOnClickListener(this);

        signupTextView = (TextView) findViewById(R.id.sign_up_button);
        signupTextView.setOnClickListener(this);
        usernameEditText = (EditText) findViewById(R.id.et_username);
        emailEditText = (EditText) findViewById(R.id.et_email);
        passwordEditText = (EditText) findViewById(R.id.et_password);
        confirmPasswordEditText = (EditText) findViewById(R.id.et_confirm_password);
        mLoadingIndicator = new ProgressDialog(this);

    }

    private boolean inputIsValid() {
        username = usernameEditText.getText().toString().trim();
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        String confirmPasswordValue = confirmPasswordEditText.getText().toString().trim();
        if (username.isEmpty()) {
            usernameEditText.setError("Field can not be empty");
            return false;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Field can not be empty");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email format");
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Field can not be empty");
            return false;
        }

        if (password.isEmpty()) {
            confirmPasswordEditText.setError("Field can not be empty");
            return false;
        }
        if (!password.equals(confirmPasswordValue)) {
            confirmPasswordEditText.setError("password does not match");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.transition_left_to_right, R.anim.transition_right_to_left);
    }

    @Override
    public void onClick(View v) {
        UIUtils.hideKeyboard(this);
        if (v.getId() == R.id.tv_login) {
            onBackPressed();
        } else if (v.getId() == R.id.sign_up_button) {
            if (inputIsValid()) {
                if(NetworkUtil.isNetworkAvailable(this)) {
                    makeNetworkCallForRegister(NetworkUtil.getRegisterUrl());

                } else {
                    UIUtils.showToast(this, NO_INTERNET_CONNECTION);
                }
            }
        }
    }

    private void makeNetworkCallForRegister(String url) {
        String passwordValue = "";
        try {
            passwordValue = PasswordUtil.SHA1(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put(USERNAME, username);
            requestBody.put(PASSWORD, passwordValue);
            requestBody.put(EMAIL, email.toLowerCase());

            mLoadingIndicator.setIndeterminate(true);
            mLoadingIndicator.setCanceledOnTouchOutside(false);
            mLoadingIndicator.setMessage("Signing up... Please wait");
            mLoadingIndicator.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST
                    , url, requestBody, this, this
            );

            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        }catch (JSONException e){

        }
    }

    private void signup() {
        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
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
                signup();
            }
        }catch (JSONException e){

        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
