package com.example.a200116_hyproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int curCondition = 1;
    //긴급 전화 번호 초기값
    String callNumber = "01099133762";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        /*ActionBar Title, SubTitle*/
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("현대 님, 환영합니다!") ;

        (new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() // start actions in UI thread
                        {
                            @Override
                            public void run()
                            {
                                ActionBar ab = getSupportActionBar() ;
                                ab.setSubtitle(getCurrentTime()); ;
                            }
                        });
                    }
                    catch (InterruptedException e)
                    {}
            }
        })).start();


        // main menu button click event
        Button main_curConditionButton = findViewById(R.id.main_curConditionButton);
        Button main_curPosButton = findViewById(R.id.main_curPosButton);
        Button main_setAlarmButton = findViewById(R.id.main_setAlarmButton);
        Button main_emergencyButton = findViewById(R.id.main_emergencyButton);

        main_curConditionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConditionActivity.class);
                startActivity(intent);
            }
        });

        main_curPosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CurPosActivity.class);
                startActivity(intent);
            }
        });

        main_setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(intent);
            }
        });

        main_emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "전화 화면 이동", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + callNumber));
                startActivity(intent);
            }
        });
    }


    // 현재 시간 string 으로 return
    public String getCurrentTime(){
        long time = System.currentTimeMillis();

        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy/MM/dd  hh:mm:ss");

        String str = dayTime.format(new Date(time));

        return str;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        ImageView btn = (ImageView) findViewById(R.id.drawer_profile_image);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(v.getId() == R.id.drawer_profile_image){
                    callProfileShow();
                }
            }
        });
        return true;
    }

    //drawer menu event
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_trafficInfo) {
            //교통정보 홈페이지 연결
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.roadplus.co.kr/main/main.do"));
            startActivity(intent);
        }
        else if (id == R.id.nav_sendStatus) {
            //메일로 상태 전송
            Toast.makeText(getApplicationContext(), "실시간 상태 아직", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_emergencyManual) {
            //교통사고 매뉴얼
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.koroad.or.kr/kp_web/accTreat.do"));
            startActivity(intent);
        }
        else if (id == R.id.nav_healthInfo) {
            //건강 상식
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.insunet.co.kr/health-guide"));
            startActivity(intent);
        }
        else if (id == R.id.nav_setCallNumber) {
            //긴급 전화 번호 설정
            callNumberShow();
        }
        return true;
    }


    void callProfileShow()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("프로필 수정");
        builder.setMessage("사진을 수정할까요?");
        builder.setNegativeButton("수정하기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //사진 설정하기
                        Toast.makeText(getApplicationContext(),"사진 설정",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setPositiveButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    void callNumberShow()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("긴급 전화 번호");
        builder.setMessage("현재 번호 : " + callNumber);
        builder.setNegativeButton("수정하기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setCallNumber();
                    }
                });
        builder.setPositiveButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    void setCallNumber()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(R.layout.set_call_number_alert_dialog);

        builder.setTitle("긴급 전화 번호");
        builder.setMessage("변경할 번호를 입력하세요");
        builder.setNegativeButton("입력",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //EditText editText = (EditText) findViewById(R.id.inputNumber);
                    //callNumber = editText.getText().toString();
                    //Toast.makeText(getApplicationContext(), editText.getText().toString(), Toast.LENGTH_LONG).show();

                }
            });

        builder.setPositiveButton("취소",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

        builder.show();
    }

}
