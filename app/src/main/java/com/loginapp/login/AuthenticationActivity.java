package com.loginapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.loginapp.login.data.UserPreference;
import com.loginapp.login.utils.AuthenticationListener;

public class AuthenticationActivity extends AppCompatActivity implements AuthenticationListener {

    FrameLayout container;
    LoginFragment loginFragment;
    RegisterFragment registerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_authentication);
            showLoginFragment();
    }

    private void showLoginFragment() {
        loginFragment = new LoginFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, loginFragment).commit();

    }

    private void showRegisterFragment() {
        registerFragment = new RegisterFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, registerFragment).commit();

    }

    @Override
    public void displayLogin() {
        showLoginFragment();
    }

    @Override
    public void displayRegister() {
        showRegisterFragment();
    }
}
