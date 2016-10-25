package com.UME.andy.ume_project;

/**
 * Created by Andy on 2016/7/11.
 */

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class decrypt extends AppCompatActivity{
    Rabbit rabbit = new Rabbit();
    Button button3;
    Button button4;
    EditText content;
    TextView textView;
    String temp_key = "abcd";
    String IV = "thisisfucking";
    boolean trimPadding = true;
    public void get_key() throws IOException{       //從檔案把KEY抓出來
        char buffer[] = new char [100];
        FileReader fr;
        File path = Environment.getExternalStorageDirectory();
        File file = new File(path,"key");
        try{
            fr = new FileReader(file);
            int len = fr.read(buffer);
            temp_key = new String(buffer,0,len);
            fr.close();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decrypt);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        content = (EditText)findViewById(R.id.editText2);
        textView = (TextView)findViewById(R.id.textView6);
        textView.setMovementMethod(new ScrollingMovementMethod());
        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if("".equals(content.getText().toString().trim())) {    //如果密文輸入的欄位是空白的 則跳出錯誤訊息
                    AlertDialog.Builder builder = new AlertDialog.Builder(decrypt.this);
                    builder.setTitle("Warning!");
                    builder.setMessage("You have not type the content or key!!");
                    builder.show();
                }
                else {
                        try {
                            get_key();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String my_content = content.getText().toString();
                        String my_key = temp_key;
                        String my_content2 = getHexToString(my_content);
                        byte[] encrypt_message = my_content2.getBytes(StandardCharsets.ISO_8859_1);
                        String decrypt_message = rabbit.decryptMessage(encrypt_message,my_key,IV,trimPadding);
                        AlertDialog.Builder builder = new AlertDialog.Builder(decrypt.this);    //跳出訊息表示明文
                        builder.setTitle("After decryption : ");
                        builder.setMessage(decrypt_message);
                        builder.show();
                        textView.append(decrypt_message);
                        textView.append("\r\n");
                        content.setText("");
                }
            }
        });

        button4.setOnClickListener(new Button.OnClickListener() {   //跳到加密的頁面
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(decrypt.this,MainActivity.class);
                startActivity(intent);
                decrypt.this.finish();
            }
        });
    }
    public String getHexToString(String strValue) {
        int intCounts = strValue.length() / 2;
        String strReturn = "";
        String strHex = "";
        int intHex = 0;
        byte byteData[] = new byte[intCounts];
        try {
            for (int intI = 0; intI < intCounts; intI++) {
                strHex = strValue.substring(0, 2);
                strValue = strValue.substring(2);
                intHex = Integer.parseInt(strHex, 16);
                if (intHex > 128)
                    intHex = intHex - 256;
                byteData[intI] = (byte) intHex;
            }
            strReturn = new String(byteData,StandardCharsets.ISO_8859_1);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return strReturn;
    }
}
