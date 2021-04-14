package com.example.a200116_hyproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class FindPWActivity extends AppCompatActivity {

    EditText findPw_id, findPw_phoneNum, findPw_carNum;
    String sFindPw_id, sFindPw_phoneNum, sFindPw_carNum;

    private Socket socket;
    BufferedReader in;
    PrintWriter out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_pw_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button find_pw_submit_button = (Button) findViewById(R.id.find_pw_submit_button);
        find_pw_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                findPw_id = (EditText) findViewById(R.id.find_pw_id);
                findPw_phoneNum = (EditText) findViewById(R.id.find_pw_phoneNum);
                findPw_carNum = (EditText) findViewById(R.id.find_pw_carNum);

                sFindPw_id = findPw_id.getText().toString();
                sFindPw_phoneNum = findPw_phoneNum.getText().toString();
                sFindPw_carNum = findPw_carNum.getText().toString();

                if (!isNumber(sFindPw_phoneNum)) {
                    Toast.makeText(getApplicationContext(), "번호는 숫자만으로 입력해주세요",
                            Toast.LENGTH_LONG).show();
                }
                else if ((sFindPw_phoneNum.length() != 11)) {
                    Toast.makeText(getApplicationContext(), "전화번호를 확인해주세요", Toast.LENGTH_LONG).show();
                }
                else if (((sFindPw_carNum.length()) != 7) && (sFindPw_carNum.length()) != 8) {
                    Toast.makeText(getApplicationContext(), "차량 번호를 확인해주세요", Toast.LENGTH_LONG).show();
                }
                else {
                    out.println("FindPW");
                    out.println(sFindPw_id);
                    out.println(sFindPw_phoneNum);
                    out.println(sFindPw_carNum);

                    String inputLine = null;
                    String userpw;

                    try {
                        inputLine = in.readLine();

                        if ("FindPW_complete".equals(inputLine)) {
                            //// 로그인 성공
                            userpw = in.readLine();
                            Toast.makeText(FindPWActivity.this,"비밀번호는 " + userpw + " 입니다.",
                                    Toast.LENGTH_LONG).show();
                            socket.close();

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                        else {
                            //// 로그인 실패 (서버 재접속을 위해 화면 새로고침)
                            Toast.makeText(FindPWActivity.this,"정보를 정확하게 입력해주세요.",
                                    Toast.LENGTH_LONG).show();
                            socket.close();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            public boolean isNumber(String str) {
                boolean check = true;

                for (int i = 0; i < str.length(); i++) {
                    if (!Character.isDigit(str.charAt(i))) {
                        check = false;
                        break;
                    }
                }
                return check;
            }
        });

        Button find_pw_id_button = (Button) findViewById(R.id.find_pw_id_button);
        find_pw_id_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindIDActivity.class);
                startActivity(intent);
            }
        });

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            socket.close();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void onStop() {
        super.onStop();
        try {
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
