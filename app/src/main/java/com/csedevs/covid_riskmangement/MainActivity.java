package com.csedevs.covid_riskmangement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button foodie, publicie;
    public static int NOTIFIER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //linking xml with the hard coded java..
        foodie = findViewById(R.id.foodie);
        publicie = findViewById(R.id.publicie);

        //taking to the respective login pages...
        foodie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, foodActivity.class));
            }
        });



        publicie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NOTIFIER = 0;
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

    }
}
