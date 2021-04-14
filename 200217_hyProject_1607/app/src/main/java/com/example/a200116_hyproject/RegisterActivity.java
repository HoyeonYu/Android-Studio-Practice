package com.example.a200116_hyproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    EditText register_name, register_birthdate, register_id, register_pw, register_pw_chk, register_phoneNum, register_carNum;
    String sName, sBirthdate, sId, sPw, sPw_chk, sPhoneNum, sCarNum, sex;

    private Socket socket;
    BufferedReader in;
    PrintWriter out;

    ImageView img_user;

    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;
    String mCurrentPhotoPath;
    Uri imageURI;
    Uri photoURI, albumURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        register_name = (EditText) findViewById(R.id.register_name);
        register_birthdate = (EditText) findViewById(R.id.register_birthdate);
        register_id = (EditText) findViewById(R.id.register_id);
        register_pw = (EditText) findViewById(R.id.register_pw);
        register_phoneNum = (EditText) findViewById(R.id.register_phoneNum);
        register_carNum = (EditText) findViewById(R.id.register_carNum);

        final ImageView register_profileImage = findViewById(R.id.register_profileImage);
        register_profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu pop = new PopupMenu(getApplicationContext(),view);
                getMenuInflater().inflate(R.menu.menu, pop.getMenu());

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.one:
                                // 실제 카메라 구동 코드는 함수로 처리
                                captureCamera();
                                break;

                            case R.id.two:
                                //갤러리에 관한 권한을 받아오는 코드
                                getAlbum();
                                break;

                            case R.id.three:
                                //기본이미지
                                register_profileImage.setImageResource(R.drawable.register_profile_icon);
                        }
                        return true;
                    }
                });
                pop.show();
                checkPermission();
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
                sPhoneNum = register_phoneNum.getText().toString();
                sCarNum = register_carNum.getText().toString();

                if ((sName.replace(" ", "").equals("")) ||
                    (sBirthdate.replace(" ", "").equals("")) ||
                    (sId.replace(" ", "").equals("")) ||
                    (sPw.replace(" ", "").equals("")) ||
                    (sPhoneNum.replace(" ", "").equals("")) ||
                    (sCarNum.replace(" ", "").equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "빈칸을 빠짐없이 입력해주세요", Toast.LENGTH_LONG).show();
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
                else if (((sCarNum.length()) != 7) && (sCarNum.length()) != 8) {
                    Toast.makeText(getApplicationContext(), "차량 번호를 확인해주세요", Toast.LENGTH_LONG).show();
                }

                else {

                    //전송 순서 ("id","pw","name","sex","birthdate","phonenumber","carnumber", "EAR")
                    out.println("Registration");
                    out.println(sId);
                    out.println(sPw);
                    out.println(sName);
                    out.println(sex);
                    out.println(sBirthdate);
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
                        try {
                            socket.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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
                    socket = new Socket("27.96.130.164", 8006);
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

        Spinner Main_spinner = (Spinner) findViewById(R.id.register_spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.성별, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Main_spinner.setAdapter(adapter);

        Main_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);

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

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            if((ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA))){
                new AlertDialog.Builder(this).setTitle("알림").setMessage("저장소 권한이 거부되었습니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package: " + getPackageName()));
                        startActivity(intent);
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setCancelable(false).create().show();
            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==0){
            if(grantResults[0]==0){
                Toast.makeText(this,"카메라 권한 승인완료",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"카메라 권한 승인 거절",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_TAKE_PHOTO:
                if(resultCode == Activity.RESULT_OK){
                    try{
                        Log.i("REQUEST_TAKE_PHOTO","OK!!!!!!");
                        galleryAddPic();

                        img_user.setImageURI(imageURI);
                    }
                    catch(Exception e){
                        Log.e("REQUEST_TAKE_PHOTO",e.toString());
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this,"저장공간에 접근할 수 없는 기기 입니다.",Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_TAKE_ALBUM:
                if(resultCode == Activity.RESULT_OK){
                    if(data.getData() != null){
                        try {
                            File albumFile = null;
                            albumFile = createImageFile();
                            photoURI = data.getData();
                            albumURI = Uri.fromFile(albumFile);
                            cropImage();
                        } catch (IOException e) {
                            Log.e("TAKE_ALBUM_SINLE_ERROR",e.toString());
                        }
                    }
                }
                break;
            case REQUEST_IMAGE_CROP:
                if(resultCode == Activity.RESULT_OK){
                    galleryAddPic();
                    //사진 변환 error
                    img_user.setImageURI(albumURI);
                }
                break;
        }
    }

    private void getAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }

    private void captureCamera() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(takePictureIntent.resolveActivity(getPackageManager()) != null){
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(photoFile != null){
                    Uri providerUri = FileProvider.getUriForFile(this,getPackageName(),photoFile);
                    imageURI = providerUri;

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,providerUri);
                    startActivityForResult(takePictureIntent,REQUEST_TAKE_PHOTO);
                }
            }
            else{
                Toast.makeText(this,"접근 불가능 합니다",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures");

        if(!storageDir.exists()){
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    //사진 crop할 수 있도록 하는 함수
    public void cropImage(){
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(photoURI,"image/*");
        cropIntent.putExtra("aspectX",1);
        cropIntent.putExtra("aspectY",1);
        cropIntent.putExtra("scale",true);
        cropIntent.putExtra("output",albumURI);
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }

    // 갤러리에 사진 추가 함수
    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        Uri contentURI = Uri.fromFile(file);
        mediaScanIntent.setData(contentURI);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this,"앨범에 저장되었습니다.",Toast.LENGTH_SHORT).show();
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