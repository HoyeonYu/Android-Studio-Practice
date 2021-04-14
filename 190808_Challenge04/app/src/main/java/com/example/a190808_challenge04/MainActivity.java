package com.example.a190808_challenge04;

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

    public void onSendButtonClicked(View v)
    {
        Toast.makeText(this, "메시지를 전송합니다.", Toast.LENGTH_LONG).show();
    }

    public void onCloseButtonClicked(View v)
    {
        Toast.makeText(this, "메시지 창을 닫습니다.", Toast.LENGTH_LONG).show();
    }

}
