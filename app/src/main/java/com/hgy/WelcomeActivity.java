package com.hgy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.hgy.main.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        initDate();
    }

    private void initDate() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ToHome();
            }
        },1000);
    }

    private void ToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
