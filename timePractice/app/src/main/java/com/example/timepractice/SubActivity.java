package com.example.timepractice;

import android.app.Activity;
import android.os.Bundle;

public class SubActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sub);  // layout xml 과 자바파일을 연결
    }


}
