package com.example.a200116_hyproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.w3c.dom.Text;

public class FindIDActivity extends AppCompatActivity {

    EditText findId_phoneNum, findId_carNum;
    String sFindId_phoneNum, sFindId_carNum;

    private Socket socket;
    BufferedReader in;
    PrintWriter out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button find_id_submit_button = (Button) findViewById(R.id.find_id_submit_button);
        find_id_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                findId_phoneNum = (EditText) findViewById(R.id.find_id_phoneNum);
                findId_carNum = (EditText) findViewById(R.id.find_id_carNum);

                sFindId_phoneNum = findId_phoneNum.getText().toString();
                sFindId_carNum = findId_carNum.getText().toString();

                if (!isNumber(sFindId_phoneNum)) {
                    Toast.makeText(getApplicationContext(), "번호는 숫자만으로 입력해주세요",
                            Toast.LENGTH_LONG).show();
                }
                else if ((sFindId_phoneNum.length() != 11)) {
                    Toast.makeText(getApplicationContext(), "전화번호를 확인해주세요", Toast.LENGTH_LONG).show();
                }
                else if (((sFindId_carNum.length()) != 7) && (sFindId_carNum.length()) != 8) {
                    Toast.makeText(getApplicationContext(), "차량 번호를 확인해주세요", Toast.LENGTH_LONG).show();
                }
                else {
                    out.println("FindID");
                    out.println(sFindId_phoneNum);
                    out.println(sFindId_carNum);

                    String inputLine = null;
                    String userId;

                    try {
                        inputLine = in.readLine();

                        if ("FindID_complete".equals(inputLine)) {
                            userId = in.readLine();
                            Toast.makeText(FindIDActivity.this,"아이디는 " + userId + " 입니다.",
                                    Toast.LENGTH_LONG).show();
                            socket.close();

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(FindIDActivity.this,"등록되지 않은 정보입니다.",
                                    Toast.LENGTH_LONG).show();
                            socket.close();

                            Intent intent = new Intent(getApplicationContext(), FindIDActivity.class);
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

        Button find_id_pw_button = (Button) findViewById(R.id.find_id_pw_button);
        find_id_pw_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    socket.close();

                    Intent intent = new Intent(getApplicationContext(), FindPWActivity.class);
                    startActivity(intent);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
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
