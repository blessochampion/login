package com.loginapp.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity implements Runnable {

    private static final long WAITING_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestFullScreen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setTextViewFont();
        new Handler().postDelayed(this, WAITING_TIME);

    }

    private void navigateToLogin() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
    }

    private void setTextViewFont() {
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/Lato-Light.ttf");
        ((TextView)findViewById(R.id.welcome)).setTypeface(tf);
    }

    private void requestFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void run() {
        navigateToLogin();
    }
}
