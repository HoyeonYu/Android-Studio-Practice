package com.jungmin.registerloginexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PassWordActivity extends AppCompatActivity {
    private EditText new_pass, new_pass_check;
    private Button btn_change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word);

        new_pass = findViewById(R.id.new_pass);
        new_pass_check = findViewById(R.id.new_pass_check);
        btn_change = findViewById(R.id.btn_change);

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userId");

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String password1 = new_pass.getText().toString();
                String password2 = new_pass_check.getText().toString();

                if(!(password1.equals(password2))){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if(success){

                                    Toast.makeText(getApplicationContext(), "비밀번호가 변경되었습니다다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PassWordActivity.this, MypageActivity.class);
                                    startActivity(intent);
                                }//if
                                else {
                                    Toast.makeText(getApplicationContext(), "비밀번호 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }//else

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }; // response listener
                    ChangePassRequest changeRequest = new ChangePassRequest(userID, password1, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(PassWordActivity.this);
                    queue.add(changeRequest);
                }
            }
        });

    }
}