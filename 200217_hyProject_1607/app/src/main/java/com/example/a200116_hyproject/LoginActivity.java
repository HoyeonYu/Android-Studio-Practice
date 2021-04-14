package com.example.a200116_hyproject;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.UserHandle;
import android.service.autofill.UserData;
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

public class LoginActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU=101;

    private Socket socket;
    BufferedReader in;
    PrintWriter out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView signIn_findText = (TextView) findViewById(R.id.signIn_findText);
        signIn_findText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), FindIDActivity.class);
                startActivityForResult(intent,REQUEST_CODE_MENU);
            }
        });

        TextView signIn_registerText = (TextView) findViewById(R.id.signIn_registerText);
        signIn_registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MENU) {
            if (resultCode == RESULT_OK) {
                String menu = data.getExtras().getString("menu");
                Toast.makeText(getApplicationContext(), "응답으로 전달된 menu :" + menu,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void login(View v){
        EditText id = (EditText) findViewById(R.id.signIn_id);
        EditText pw = (EditText) findViewById(R.id.signIn_pw);

        String userid = id.getText().toString();
        String userpw = pw.getText().toString();

        out.println("Login");
        out.println(userid);
        out.println(userpw);

        String inputLine = null;

        try {
            inputLine = in.readLine();

            if ("Login_complete".equals(inputLine)) {
                //// 로그인 성공
                userid = in.readLine();
                socket.close();

                Intent intentID = new Intent(LoginActivity.this, MainActivity.class);
                intentID.putExtra("userid", userid);
                startActivity(intentID);
            }
            else {
                //// 로그인 실패 (서버 재접속을 위해 화면 새로고침)
                Toast.makeText(LoginActivity.this,"로그인 실패", Toast.LENGTH_LONG).show();
                socket.close();

                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /*
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
    */
}