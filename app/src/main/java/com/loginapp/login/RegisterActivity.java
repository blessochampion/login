package com.loginapp.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.loginapp.login.utils.UIUtils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView loginTextView;
    TextView signupTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginTextView = (TextView) findViewById(R.id.tv_login);
        loginTextView.setOnClickListener(this);

        signupTextView = (TextView) findViewById(R.id.sign_up_button);
        signupTextView.setOnClickListener(this);
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
            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
            overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
        }
    }
}
