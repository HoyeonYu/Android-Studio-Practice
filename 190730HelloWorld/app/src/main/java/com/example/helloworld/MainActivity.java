package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    //onCreate()함수는 main()함수 역할
    {
        super.onCreate(savedInstanceState);
        //super 키워드는 onCreate()함수 호출

        setContentView(R.layout.activity_main);
        //Hello World! 메시지는 R.layout.activity_main 에 의해 나타남
        //메시지는 activity_main.xml - text 에서 변경 가능
        //activity_main.xml 은 앱의 첫 화면 의미
    }

    public void onButton1Clicked(View v)
    {
        Toast.makeText(this, "확인 1 버튼이 눌렸어요.", Toast.LENGTH_LONG).show();
        //Toast 는 작고 간단한 메시지를 잠깐 보여주는 역할
    }

    public void onButton2Clicked(View v)
    {
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.naver.com"));
        startActivity(myIntent);
        //intent 는 앱 구성요소간 데이터 전달, 실행하려는 기능 안드로이드 플랫폼에 알림
    }

    public void onButton3Clicked(View v)
    {
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-1000-1000"));
        startActivity(myIntent);
    }
}
