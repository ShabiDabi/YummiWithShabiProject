package com.example.mor.yammiwithshabi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent intent = getIntent();
        String name = intent.getStringExtra(MainActivity.LOG_IN_NAME);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.nameView);
        textView.setText("Welcome " + name);
    }
}
