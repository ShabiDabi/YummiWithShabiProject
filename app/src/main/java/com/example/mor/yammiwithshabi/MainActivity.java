package com.example.mor.yammiwithshabi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_IN_NAME = "com.example.myfirstapp.Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void logIn(View view){
        Intent intent = new Intent(this, HomePageActivity.class);
        EditText editText = (EditText)findViewById(R.id.editText);
        String loginName = editText.getText().toString();
        intent.putExtra(LOG_IN_NAME, loginName);
        startActivity(intent);
    }
}
