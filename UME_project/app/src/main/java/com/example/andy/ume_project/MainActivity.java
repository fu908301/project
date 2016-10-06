package com.example.andy.ume_project;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.UME.andy.ume_project.R;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    Button button1;
    Button button2;
    EditText key;
    int checkindex = 0;     //checkindex判斷說是否有按下Confirm按鈕
    public static String byteArrayToHexString(byte[] b) {//把SHA1的亂碼轉成16進位用
        String result = "";
        for (int i=0; i < b.length; i++) {
            result +=
                    Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }
    public static String toSHA1(byte[] convertme) {//SHA1的HASH演算法
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return byteArrayToHexString(md.digest(convertme));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        key = (EditText) findViewById(R.id.editText);
        verifyStoragePermissions(this);
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(key.getText().toString().trim())) {       //當輸入行為空  跳出錯誤訊息
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Warning!");
                    builder.setMessage("You have not type the key!!");
                    builder.show();
                }
                else{
                    checkindex = 1;     //因為按了confirm 所以變成1

                    try {
                        toNext();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);   //跳出警示訊息表示有輸入
                    builder.setTitle("Your key is : ");
                    String S1 = key.getText().toString();
                    String S2 = toSHA1(S1.getBytes());
                    System.out.println(S2);
                    builder.setMessage("Fuck you");
                    builder.show();
                }
            }
        });
        button2.setOnClickListener(new Button.OnClickListener() {       //跳到decrypt頁面
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,decrypt.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }
    public void toNext() throws IOException {       //把輸入的Key跟checkindex存入檔案
        String stringindex = Integer.toString(checkindex);
        String hashkey =toSHA1(key.getText().toString().getBytes());
        File path = Environment.getExternalStorageDirectory();
        File file1 = new File(path, "key");
        File file2 = new File(path, "checkindex");
        FileWriter fw1 = new FileWriter(file1,false);
        FileWriter fw2 = new FileWriter(file2,false);
        fw1.write(hashkey);
        fw2.write(stringindex);
        fw1.close();
        fw2.close();
    }
    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}


