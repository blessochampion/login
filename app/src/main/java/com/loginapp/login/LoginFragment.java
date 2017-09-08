package com.loginapp.login;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
public class LoginFragment extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
    public static final String NO_INTERNET_CONNECTION = "No internet Connection";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String DATA = "data";
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_POINT = "point";
    TextView signupTextView;
    TextView loginTextView;
    EditText username;
    EditText password;
    ProgressDialog mLoadingIndicator;
    AuthenticationListener listener;

    public LoginFragment() {
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
       View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        getReferencesToViews(rootView);
        return  rootView;
    }

    private void getReferencesToViews(View rootView){
        signupTextView = (TextView) rootView.findViewById(R.id.tv_sign_up);
        signupTextView.setOnClickListener(this);

        loginTextView = (TextView) rootView.findViewById(R.id.login_button);
        loginTextView.setOnClickListener(this);
        username = (EditText) rootView.findViewById(R.id.username);
        password = (EditText) rootView.findViewById(R.id.password);
        mLoadingIndicator = new ProgressDialog(getActivity());

    }

    @Override
    public void onClick(View v) {
        UIUtils.hideKeyboard(getActivity());
        if (v.getId() == R.id.tv_sign_up) {
            signup();
        } else if (v.getId() == R.id.login_button) {


            if (inputIsValid()) {
                if(NetworkUtil.isNetworkAvailable(getActivity())) {
                    makeNetworkCallForLogin(NetworkUtil.getLoginUrl());


                } else {
                    UIUtils.showToast(getActivity(), NO_INTERNET_CONNECTION);
                }
            }

        }
    }

    private void signup() {
       listener.displayRegister();
        getActivity().overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
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

    private void makeNetworkCallForLogin(String url) {

        String passwordValue = "";
        try {
            passwordValue =   PasswordUtil.SHA1(password.getText().toString().trim());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put(USERNAME, username.getText().toString().trim());
            requestBody.put(PASSWORD, passwordValue);

            mLoadingIndicator.setIndeterminate(true);
            mLoadingIndicator.setCanceledOnTouchOutside(false);
            mLoadingIndicator.setMessage("Loging you in... Please wait");
            mLoadingIndicator.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST
                    , url, requestBody, this, this
            );

            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        }catch (JSONException e){

        }
    }

    @Override
    public void onResponse(JSONObject response) {
        if(mLoadingIndicator != null && mLoadingIndicator.isShowing()){
            mLoadingIndicator.cancel();
        }
        try{
            int code = response.getInt(CODE);
            if(code == NetworkUtil.FAILURE_RESPONSE_CODE){
                UIUtils.showToast(getActivity(), response.getString(DESCRIPTION));
            }else {
                JSONObject data = response.getJSONObject(DATA);
                UserPreference userPreference = UserPreference.getInstance(getActivity());
                userPreference.setUserId(data.getInt(KEY_ID));
                userPreference.setUsername(data.getString(KEY_USERNAME));
                userPreference.setUserPoint(data.getInt(KEY_POINT));
                login();
                Log.e("ddd", response.toString());
            }
        }catch (JSONException e){

        }
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        if(mLoadingIndicator != null && mLoadingIndicator.isShowing()){
            mLoadingIndicator.cancel();
        }
        UIUtils.showToast(getActivity(), "Something Went Wrong... Please try again");
    }

    private void login() {
        UserPreference userPreference = UserPreference.getInstance(getActivity());
        userPreference.setUserLoggedIn(true);
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
    }
}
