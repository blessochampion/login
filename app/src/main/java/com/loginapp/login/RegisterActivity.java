package com.loginapp.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loginapp.login.utils.UIUtils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView loginTextView;
    TextView signupTextView;
    EditText usernameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;



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

    }

    private boolean inputIsValid() {
        String usernameValue = usernameEditText.getText().toString().trim();
        String emailValue = emailEditText.getText().toString().trim();
        String passwordValue = passwordEditText.getText().toString().trim();
        String confirmPasswordValue = confirmPasswordEditText.getText().toString().trim();
        if(usernameValue.isEmpty()){
            usernameEditText.setError("Field can not be empty");
            return false;
        }

        if(emailValue.isEmpty()){
            emailEditText.setError("Field can not be empty");
            return false;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()){
            emailEditText.setError("Invalid email format");
            return false;
        }

        if(passwordValue.isEmpty()){
            passwordEditText.setError("Field can not be empty");
            return  false;
        }

        if(passwordValue.isEmpty()){
            confirmPasswordEditText.setError("Field can not be empty");
            return  false;
        }
        if(!passwordValue.equals(confirmPasswordValue)){
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
            if(inputIsValid()) {
                signup();
            }
        }
    }

    private void signup() {
        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.transition_enter, R.anim.transition_exit);
    }
}
