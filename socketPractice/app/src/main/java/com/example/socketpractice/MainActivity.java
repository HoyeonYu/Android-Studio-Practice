package com.example.socketpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private Connection conn = null;
    String id,pw;
    boolean insertdata = true;
    ResultSet rs;
    static String n;

    public void DBLogin () {
        this.id = findViewById(R.id.login_id).toString();
        this.pw = findViewById(R.id.login_pw).toString();
        conn = getConnection();
        close();
    }

    public Connection getConnection() {
        String url = "jdbc:mysql://localhost:3307/refrigerator_manager";
        String user = "root";   // 사용자명과 패스워드 입력.
        String pwd = "juneo";

        if(conn == null){
            try {
                Class.forName("org.gjt.mm.mysql.Driver"); //mysql 드라이버 로딩
                conn = DriverManager.getConnection(url, user, pwd); // 주어진 DB에 연결하고 객체를 반환

                try {
                    Statement stmt = conn.createStatement();
                    stmt.execute("use refrigerator_manager");
                    rs = stmt.executeQuery("SELECT NAME FROM USERLIST WHERE ID='"+id+"'AND PASSWORD='"+pw+"'");
                    while (rs.next()) {
                        n = rs.getString("NAME");
                    }
                }
                catch(Exception e){System.out.println(e.toString());}

            } catch (Exception e){System.out.println(e.toString());}
        }

        return conn;
    }

    public void close() {
        if(conn != null){ 	//conn이 null이 아니라는 것은 DB와 연결되어있다는 의미.
            try {
                if(!conn.isClosed())  // inClose 닫혀있는지 확인
                    conn.close();
            } catch (Exception e){System.out.println(e.toString());}
        }
        conn = null;  // 닫혀지면 conn 값을 null로 설정
    }
}
