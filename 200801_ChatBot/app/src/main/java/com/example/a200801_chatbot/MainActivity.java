package com.example.a200801_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClicked_SendButton(View view) {
        Toast.makeText(getApplicationContext(), "Send", Toast.LENGTH_LONG).show();
    }
}
