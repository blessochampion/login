package com.loginapp.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loginapp.login.utils.UIUtils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView pointTextView;
    TextView addButton;
    EditText contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pointTextView = (TextView) findViewById(R.id.tv_point);
        contentEditText = (EditText) findViewById(R.id.et_contents);
        addButton = (TextView) findViewById(R.id.add_button);
        addButton.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_button) {
            deductPoint();
            UIUtils.hideKeyboard(this);
        }
    }

    private void deductPoint() {
        if (!contentEditText.getText().toString().isEmpty()) {
            int currentValue = Integer.valueOf(pointTextView.getText().toString());
            if (currentValue > 0) {
                currentValue--;
                pointTextView.setText(String.valueOf(currentValue));
            } else {
                contentEditText.setError("You do not have enough points");
            }
        } else {
            contentEditText.setError("Field Can not be empty");
        }
    }
}
