package com.loginapp.login;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loginapp.login.utils.UIUtils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String HOME = "Home";
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(HOME);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
                finish();
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }
}
