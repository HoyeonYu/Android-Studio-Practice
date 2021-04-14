package com.jungmin.registerloginexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //private TextView tv_id, tv_pass;
    //private Button btn_mypage;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag1 frag1;
    private Frag2 frag2;
    private Frag3 frag3;
    private Frag4 frag4;
    private Frag5 frag5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // tv_id = findViewById(R.id.tv_id);
       // tv_pass = findViewById(R.id.tv_pass);
       // btn_mypage = findViewById(R.id.btn_mypage);
        //Intent intent = getIntent();
        //String userID = intent.getStringExtra("userID");
        //String userPass = intent.getStringExtra("userPass");

      //  tv_id.setText(userID);
      //  tv_pass.setText(userPass);

      //  btn_mypage.setOnClickListener(new View.OnClickListener() {
           // @Override
//
  //          public void onClick(View view) {
    //            String userID = tv_id.getText().toString();
      //          String userPass = tv_pass.getText().toString();

        //        Intent intent = new Intent(MainActivity.this, MypageActivity.class);
          //      intent.putExtra("userID",userID);
            //    intent.putExtra("userPass",userPass);
              //  startActivity(intent);
            //}
        //});
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_a:
                        setFrag(0);
                        break;
                    case R.id.action_b:
                        setFrag(1);
                        break;
                    case R.id.action_c:
                        setFrag(2);
                        break;
                    case R.id.action_d:
                        setFrag(3);
                        break;
                    case R.id.action_mypage:
                        setFrag(4);
                        break;
                }
                return true;
            }
        });


        frag1 = new Frag1();
        frag2 = new Frag2();
        frag3 = new Frag3();
        frag4 = new Frag4();
        frag5 = new Frag5();
        setFrag(0); //첫 프래그먼트 화면 지정
    }

    private void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch(n){
            case 0:
                ft.replace(R.id.main_frame, frag1);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, frag2);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, frag3);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame, frag4);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.main_frame, frag5);
                ft.commit();
                break;

        }
    }
}