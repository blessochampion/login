package com.loginapp.login;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loginapp.login.data.UserPreference;
import com.loginapp.login.utils.AuthenticationListener;
import com.loginapp.login.utils.NetworkUtil;
import com.loginapp.login.utils.PasswordUtil;
import com.loginapp.login.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {

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
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_POINT = "point";
    public static final String DATA = "data";
    private String email;
    private String username;
    private String password;
    ProgressDialog mLoadingIndicator;
    AuthenticationListener listener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AuthenticationListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        getReferencesToViews(rootView);
        return rootView;
    }

    public void getReferencesToViews(View rootView) {

        loginTextView = (TextView) rootView.findViewById(R.id.tv_login);
        loginTextView.setOnClickListener(this);

        signupTextView = (TextView) rootView.findViewById(R.id.sign_up_button);
        signupTextView.setOnClickListener(this);
        usernameEditText = (EditText) rootView.findViewById(R.id.et_username);
        emailEditText = (EditText) rootView.findViewById(R.id.et_email);
        passwordEditText = (EditText) rootView.findViewById(R.id.et_password);
        confirmPasswordEditText = (EditText) rootView.findViewById(R.id.et_confirm_password);
        mLoadingIndicator = new ProgressDialog(getActivity());

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
    public void onClick(View v) {
        UIUtils.hideKeyboard(getActivity());
        if (v.getId() == R.id.tv_login) {
           login();
        } else if (v.getId() == R.id.sign_up_button) {
            if (inputIsValid()) {
                if (NetworkUtil.isNetworkAvailable(getActivity())) {
                    makeNetworkCallForRegister(NetworkUtil.getRegisterUrl());

                } else {
                    UIUtils.showToast(getActivity(), NO_INTERNET_CONNECTION);
                }
            }
        }
    }

    private void login() {
        listener.displayLogin();
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
        } catch (JSONException e) {

        }
    }

    @Override
    public void onResponse(JSONObject response) {

        if (mLoadingIndicator != null && mLoadingIndicator.isShowing()) {
            mLoadingIndicator.cancel();
        }
        try {
            int code = response.getInt(CODE);
            if (code == NetworkUtil.FAILURE_RESPONSE_CODE) {
                UIUtils.showToast(getActivity(), response.getString(DESCRIPTION));
            } else {
                JSONObject data = response.getJSONObject(DATA);
                UserPreference userPreference = UserPreference.getInstance(getActivity());
                userPreference.setUserId(data.getInt(KEY_ID));
                userPreference.setUsername(data.getString(KEY_USERNAME));
                userPreference.setUserPoint(data.getInt(KEY_POINT));
                signup();
            }
        } catch (JSONException e) {

        }
    }

    private void signup() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (mLoadingIndicator != null && mLoadingIndicator.isShowing()) {
            mLoadingIndicator.cancel();
        }
        UIUtils.showToast(getActivity(), "Something Went Wrong... Please try again");
    }
}
