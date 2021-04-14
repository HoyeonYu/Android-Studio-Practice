package com.example.a200116_hyproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RegisterActivity extends AppCompatActivity {
    EditText register_name, register_birthdate, register_id, register_pw, register_pw_chk, register_phoneNum, register_carNum;
    String sName, sBirthdate, sId, sPw, sPw_chk, sPhoneNum, sCarNum, sex;

    private Socket socket;
    BufferedReader in;
    PrintWriter out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        register_name = (EditText) findViewById(R.id.register_name);
        register_birthdate = (EditText) findViewById(R.id.register_birthdate);
        register_id = (EditText) findViewById(R.id.register_id);
        register_pw = (EditText) findViewById(R.id.register_pw);
        register_pw_chk = (EditText) findViewById(R.id.register_pw_chk);
        register_phoneNum = (EditText) findViewById(R.id.register_phoneNum);
        register_carNum = (EditText) findViewById(R.id.register_carNum);

        Button register_faceRegisterButton = findViewById(R.id.register_faceRegisterButton);
        register_faceRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "얼굴을 등록합니다.\n카메라를 봐주세요",
                        Toast.LENGTH_LONG).show();
            }
        });

        Button register_RegisterButton = findViewById(R.id.register_RegisterButton);
        register_RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sName = register_name.getText().toString();
                sBirthdate = register_birthdate.getText().toString();
                sId = register_id.getText().toString();
                sPw = register_pw.getText().toString();
                sPw_chk = register_pw_chk.getText().toString();
                sPhoneNum = register_phoneNum.getText().toString();
                sCarNum = register_carNum.getText().toString();

                if ((sName.replace(" ", "").equals("")) ||
                        (sBirthdate.replace(" ", "").equals("")) ||
                        (sId.replace(" ", "").equals("")) ||
                        (sPw.replace(" ", "").equals("")) ||
                        (sPw_chk.replace(" ", "").equals("")) ||
                        (sPhoneNum.replace(" ", "").equals("")) ||
                        (sCarNum.replace(" ", "").equals(""))) {
                    Toast.makeText(getApplicationContext(), "빈칸을 빠짐없이 입력해주세요", Toast.LENGTH_LONG).show();
                }
                else if (((sName.length()) > 4) || ((sName.length()) < 3)) {
                    Toast.makeText(getApplicationContext(), "이름을 정확히 입력해주세요",
                            Toast.LENGTH_LONG).show();
                }
                else if (((sBirthdate.length()) != 6) && (!isNumber(sBirthdate))) {
                    Toast.makeText(getApplicationContext(), "생년월일을 6자리로 입력해주세요",
                            Toast.LENGTH_LONG).show();
                }
                else if (((sId.length()) <= 3)) {
                    Toast.makeText(getApplicationContext(), "아이디는 네 글자 이상으로 지어주세요",
                            Toast.LENGTH_LONG).show();
                }
                else if (((sPw.length()) <= 7)) {
                    Toast.makeText(getApplicationContext(), "비밀번호는 여덟 글자 이상으로 지어주세요",
                            Toast.LENGTH_LONG).show();
                }
                else if (!isNumber(sPhoneNum)) {
                    Toast.makeText(getApplicationContext(), "번호는 숫자만으로 입력해주세요",
                            Toast.LENGTH_LONG).show();
                }
                else if ((sPhoneNum.length() != 11)) {
                    Toast.makeText(getApplicationContext(), "전화번호를 확인해주세요", Toast.LENGTH_LONG).show();
                }
                else if (!sPw.equals(sPw_chk)) {
                    Toast.makeText(getApplicationContext(), "같은 비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                }
                else if (((sCarNum.length()) != 7) && (sCarNum.length()) != 8) {
                    Toast.makeText(getApplicationContext(), "차량 번호를 확인해주세요", Toast.LENGTH_LONG).show();
                }

                else {
                    //EditText 에서 받은 String 으로 보내야 함
                    out.println(sName);
                    out.println(sex);
                    out.println(sBirthdate);
                    out.println(sId);
                    out.println(sPw);
                    out.println(sPhoneNum);
                    out.println(sCarNum);

                    String inputLine = null;

                    try {
                        inputLine = in.readLine();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    if ("Registration_complete".equals(inputLine)) {
                        Toast.makeText(getApplicationContext(), "등록되었습니다!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
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

        Thread th = new Thread() {
            public void run() {
                try {
                    socket = new Socket("27.96.130.164", 8000);
                    out = new PrintWriter(socket.getOutputStream(), true);                                                                                                                  //전송한다.
                    in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        th.start();

        Spinner Main_spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.성별, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Main_spinner.setAdapter(adapter);

        Main_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sex = "man";
                }
                else {
                    sex = "woman";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sex = "man";
            }
        });

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