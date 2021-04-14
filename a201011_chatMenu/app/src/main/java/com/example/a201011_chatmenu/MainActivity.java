package com.example.a201011_chatmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    private Intent kakaoIntent;
    private final String packageName = "com.kakao.talk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kakaoIntent = this.getPackageManager().getLaunchIntentForPackage(packageName);
        ImageView load = (ImageView)findViewById(R.id.chat_image);
        Glide.with(this).load(R.drawable.chat_image).into(load);
    }

    public void kakaoButtonClick(View view) {
        MainActivity.this.startActivity(kakaoIntent);
    }
}
