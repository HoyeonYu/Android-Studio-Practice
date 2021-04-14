package com.example.a200116_hyproject;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.service.autofill.UserData;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU=101;

    String userid="1";
    String userpw="2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

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
        EditText id=(EditText)findViewById(R.id.signIn_id);
        EditText pw=(EditText)findViewById(R.id.signIn_pw);

        if(id.getText().toString().equals(userid)){
            if (pw.getText().toString().equals(userpw)){
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent,REQUEST_CODE_MENU);
            }
            else Toast.makeText(LoginActivity.this,"로그인 실패", Toast.LENGTH_LONG).show();
        }
        else Toast.makeText(LoginActivity.this,"로그인 실패", Toast.LENGTH_LONG).show();
    }
}