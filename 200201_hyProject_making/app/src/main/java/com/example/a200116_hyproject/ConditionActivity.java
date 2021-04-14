package com.example.a200116_hyproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConditionActivity extends AppCompatActivity {

    private Socket socket;
    BufferedReader in;
    PrintWriter out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.condition_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle intent = getIntent().getExtras();
        String userid = intent.getString("userid");

        TextView condition_pulseRate = (TextView) findViewById(R.id.condition_pulseRate);
        condition_pulseRate.setText("심박수 : " + userid);

        Thread th = new Thread() {
            public void run() {
                try {
                    socket = new Socket("27.96.130.164", 8006);
                    out = new PrintWriter(socket.getOutputStream(), true);                                                                                                                  //�쟾�넚�븳�떎.
                    in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();

    }

    public void getPulseRate(View v) {
        Bundle intentID = getIntent().getExtras();
        String userid = intentID.getString("userid");

        out.println("GetPulse");
        out.println(userid);

        String pulse;
        String inputLine = null;

        try {
            inputLine = in.readLine();

            if ("GetPulse_complete".equals(inputLine)) {
                pulse = in.readLine();
                TextView condition_pulseRate = (TextView) findViewById(R.id.condition_pulseRate);
                condition_pulseRate.setText("심박수 : " + pulse);
                Toast.makeText(ConditionActivity.this,"Pulse : " + pulse, Toast.LENGTH_LONG).show();
                //socket.close();
                //Intent intent = new Intent(ConditionActivity.this, ConditionActivity.class);
                //startActivity(intent);
            }
            else {
                Toast.makeText(ConditionActivity.this,"Pulse Error", Toast.LENGTH_LONG).show();
                //socket.close();
                //Intent intent = new Intent(ConditionActivity.this, ConditionActivity.class);
                //startActivity(intent);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getStressRate (View v) {
        Bundle intentID = getIntent().getExtras();
        String userid = intentID.getString("userid");

        out.println("GetStressRate");
        out.println(userid);

        String stress;
        String inputLine = null;

        try {
            inputLine = in.readLine();

            if ("GetStressRate_complete".equals(inputLine)) {
                stress = in.readLine();
                TextView condition_stressRate = (TextView) findViewById(R.id.condition_stressRate);
                condition_stressRate.setText("스트레스 : " + stress);
                socket.close();
                Intent intent = new Intent(ConditionActivity.this, ConditionActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(ConditionActivity.this,"Stress Error", Toast.LENGTH_LONG).show();
                socket.close();
                Intent intent = new Intent(ConditionActivity.this, ConditionActivity.class);
                startActivity(intent);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
