package com.loginapp.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loginapp.login.utils.UIUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView signupTextView;
    TextView loginTextView;
    EditText username;
    EditText password;

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
    }


    @Override
    public void onClick(View v) {
        UIUtils.hideKeyboard(this);
        if (v.getId() == R.id.tv_sign_up) {
            signup();
        } else if (v.getId() == R.id.login_button) {
            if (inputIsValid())
                login();
        }
    }

    private boolean inputIsValid() {
        String usernameValue = username.getText().toString();
        String passwordValue = password.getText().toString();
        if(usernameValue.isEmpty()){
            username.setError("Field can not be empty");
        }
        if(passwordValue.isEmpty()){
            password.setError("Field can not be empty");
        }
        return !usernameValue.isEmpty() && !passwordValue.isEmpty();
    }

    private void login() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
    }

    private void signup() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
    }
}
