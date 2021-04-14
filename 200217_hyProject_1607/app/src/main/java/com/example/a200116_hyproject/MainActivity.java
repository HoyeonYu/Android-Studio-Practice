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
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Socket socket;
    BufferedReader in;
    PrintWriter out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //Get ID from LoginActivity By intent
        //Cannot Set Global Variable
        Bundle intentID = getIntent().getExtras();
        String id = intentID.getString("userid");

        //ActionBar Title, SubTitle
        ActionBar ab = getSupportActionBar();
        ab.setTitle(id + " 님, 환영합니다!");

        final Thread th = new Thread() {
            public void run() {
                try {
                    socket = new Socket("27.96.130.164", 8006);
                    out = new PrintWriter(socket.getOutputStream(), true);                                                                                                                  //�쟾�넚�븳�떎.
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();

        // Main Menu Button Click Event
        Button main_checkDriverButton = findViewById(R.id.main_checkDriverButton);
        Button main_refresh = findViewById(R.id.main_refresh);

        Button main_curPosButton = findViewById(R.id.main_curPosButton);
        Button main_setAlarmButton = findViewById(R.id.main_setAlarmButton);
        Button main_emergencyButton = findViewById(R.id.main_emergencyButton);

        //Check Driver ID
        main_checkDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDriverID();
            }
        });

        main_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPulse();
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
                try {
                    Thread th = new Thread() {
                        public void run() {
                            try {
                                socket = new Socket("27.96.130.164", 8006);
                                out = new PrintWriter(socket.getOutputStream(), true);                                                                                                                  //�쟾�넚�븳�떎.
                                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    socket.close();
                    th.start();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(intent);
            }
        });

        main_emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoneNumber();
            }
        });

        final SwipeRefreshLayout swipe_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Bundle intent = getIntent().getExtras();
                String id = intent.getString("userid");

                out.println("GetHC");
                out.println(id);

                String inputLine = null;
                String HC;

                Button refreshButton = (Button) findViewById(R.id.main_refresh);

                try {
                    inputLine = in.readLine();

                    if ("GetHC_complete".equals(inputLine)) {
                        HC = in.readLine();

                        //result : GOOD, NORMAL, CAUTION, WARNING
                        if (HC.equals("GOOD")) {
                            refreshButton.setBackground(ContextCompat.getDrawable(MainActivity.this,
                                    R.drawable.main_button_blue));
                        }
                        else if (HC.equals("NORMAL")) {
                            refreshButton.setBackground(ContextCompat.getDrawable(MainActivity.this,
                                    R.drawable.main_button_green));
                        }
                        else if (HC.equals("CAUTION")) {
                            refreshButton.setBackground(ContextCompat.getDrawable(MainActivity.this,
                                    R.drawable.main_button_yellow));
                        }
                        else if (HC.equals("WARNING")) {
                            refreshButton.setBackground(ContextCompat.getDrawable(MainActivity.this,
                                    R.drawable.main_button_red));

                            socket.close();
                        }
                    }
                    else {
                        refreshButton.setBackground(ContextCompat.getDrawable(MainActivity.this,
                                R.drawable.main_button_error));
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                swipe_layout.setRefreshing(false);

                Thread th = new Thread() {
                    public void run() {
                        try {
                            socket = new Socket("27.96.130.164", 8006);
                            out = new PrintWriter(socket.getOutputStream(), true);                                                                                                                  //�쟾�넚�븳�떎.
                            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                th.start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    //This is Similar with onCreate
    //But the Menu on Drawer Navigation have to be Changed only at onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        getCarNumber();
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

            Bundle intentID = getIntent().getExtras();
            String userid = intentID.getString("userid");

            out.println("GetPhoneNumber");
            out.println(userid);

            String inputLine = null;
            String phoneNumber;

            try {
                inputLine = in.readLine();

                Thread th = new Thread() {
                    public void run() {
                        try {
                            socket = new Socket("27.96.130.164", 8006);
                            out = new PrintWriter(socket.getOutputStream(), true);                                                                                                                  //�쟾�넚�븳�떎.
                            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                if ("GetPhoneNumber_complete".equals(inputLine)) {
                    phoneNumber = in.readLine();
                    String condition;

                    Button main_refresh = (Button) findViewById(R.id.main_refresh);

                    if (main_refresh.getBackground().getConstantState().
                            equals(getResources().getDrawable(R.drawable.main_button_blue).getConstantState())) {
                        condition = "건강";
                        getPhoneNumberSMS(phoneNumber, condition);
                    }
                    else if (main_refresh.getBackground().getConstantState().
                            equals(getResources().getDrawable(R.drawable.main_button_green).getConstantState())) {
                        condition = "보통";
                        getPhoneNumberSMS(phoneNumber, condition);
                    }
                    else if (main_refresh.getBackground().getConstantState().
                            equals(getResources().getDrawable(R.drawable.main_button_yellow).getConstantState())) {
                        condition = "주의";
                        getPhoneNumberSMS(phoneNumber, condition);
                    }
                    else if (main_refresh.getBackground().getConstantState().
                            equals(getResources().getDrawable(R.drawable.main_button_red).getConstantState())) {
                        condition = "위험";
                        getPhoneNumberSMS(phoneNumber, condition);
                    }
                    else {
                        condition = "운전자 정보 없음";
                        getPhoneNumberSMS(phoneNumber, condition);
                    }

                    socket.close();
                    th.start();

                }
                else {
                    socket.close();
                    th.start();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
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
        return true;
    }

    public void getDriverID() {

        Bundle intent = getIntent().getExtras();
        String id = intent.getString("userid");

        out.println("GetDriverID");
        out.println(id);

        String inputLine = null;
        String driverID;

        try {
            inputLine = in.readLine();

            Thread th = new Thread() {
                public void run() {
                    try {
                        socket = new Socket("27.96.130.164", 8006);
                        out = new PrintWriter(socket.getOutputStream(), true);                                                                                                                  //�쟾�넚�븳�떎.
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            if ("GetDriverID_complete".equals(inputLine)) {
                driverID = in.readLine();

                TextView main_checkDriverText = (TextView) findViewById(R.id.main_checkDriverText);
                main_checkDriverText.setText("ID " + driverID + "님");
                Toast.makeText(getApplicationContext(), "ID " + driverID + "님", Toast.LENGTH_SHORT).show();

                socket.close();
                th.start();
            }
            else {
                TextView main_checkDriverText = (TextView) findViewById(R.id.main_checkDriverText);
                main_checkDriverText.setText("운전자 정보 없음");
                Toast.makeText(getApplicationContext(), "운전자 정보 없음", Toast.LENGTH_SHORT).show();

                socket.close();
                th.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getCarNumber() {

        Bundle intent = getIntent().getExtras();
        String id = intent.getString("userid");

        out.println("GetCarNumber");
        out.println(id);

        String inputLine = null;
        String carNumber;

        try {
            inputLine = in.readLine();

            Thread th = new Thread() {
                public void run() {
                    try {
                        socket = new Socket("27.96.130.164", 8006);
                        out = new PrintWriter(socket.getOutputStream(), true);                                                                                                                  //�쟾�넚�븳�떎.
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            if ("GetCarNumber_complete".equals(inputLine)) {
                carNumber = in.readLine();

                TextView drawer_profile_name = (TextView) findViewById(R.id.drawer_profile_name);
                drawer_profile_name.setText(id + " 님");

                TextView drawer_profile_carNum = (TextView) findViewById(R.id.drawer_profile_carNum);
                drawer_profile_carNum.setText("차량번호 " + carNumber);
                socket.close();

                th.start();

            }
            else {
                Toast.makeText(MainActivity.this,"아이디 오류",
                        Toast.LENGTH_LONG).show();
                socket.close();
                th.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPulse() {

        Bundle intent = getIntent().getExtras();
        String id = intent.getString("userid");

        out.println("GetPulse");
        out.println(id);

        String inputLine = null;
        String pulse;

        try {
            inputLine = in.readLine();

            Thread th = new Thread() {
                public void run() {
                    try {
                        socket = new Socket("27.96.130.164", 8006);
                        out = new PrintWriter(socket.getOutputStream(), true);                                                                                                                  //�쟾�넚�븳�떎.
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            if ("GetPulse_complete".equals(inputLine)) {
                pulse = in.readLine();

                Toast.makeText(getApplicationContext(), "심박수 : " + pulse + "bpm", Toast.LENGTH_LONG).show();

                socket.close();
                th.start();
            }
            else {
                Toast.makeText(getApplicationContext(), "심박수 정보 없음", Toast.LENGTH_LONG).show();
                socket.close();
                th.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getPhoneNumber() {

        Bundle intentID = getIntent().getExtras();
        String id = intentID.getString("userid");

        out.println("GetPhoneNumber");
        out.println(id);

        String inputLine = null;
        String phoneNumber;

        try {
            inputLine = in.readLine();

            Thread th = new Thread() {
                public void run() {
                    try {
                        socket = new Socket("27.96.130.164", 8006);
                        out = new PrintWriter(socket.getOutputStream(), true);                                                                                                                  //�쟾�넚�븳�떎.
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            if ("GetPhoneNumber_complete".equals(inputLine)) {
                phoneNumber = in.readLine();

                Toast.makeText(getApplicationContext(), phoneNumber + ", 전화 화면 이동",
                        Toast.LENGTH_SHORT).show();

                socket.close();

                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
                th.start();
            }
            else {
                Toast.makeText(getApplicationContext(), "전화 정보 없음", Toast.LENGTH_LONG).show();
                socket.close();
                th.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPhoneNumberSMS(String phoneNumber, String condition) {

        Bundle intentID = getIntent().getExtras();
        String id = intentID.getString("userid");

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null,
                id + "님의 상태는 " + condition + " 입니다", null, null);
        Toast.makeText(getApplicationContext(), phoneNumber + ", 메시지 전송 완료", Toast.LENGTH_LONG).show();
    }

    protected void onStop() {
        super.onStop();

        try {
            socket.close();
        }
        catch (IOException e) {
            out.println("Quit");
            e.printStackTrace();
        }
    }
}