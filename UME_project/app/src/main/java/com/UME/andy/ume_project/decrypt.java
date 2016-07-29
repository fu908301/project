package com.UME.andy.ume_project;

/**
 * Created by Andy on 2016/7/11.
 */

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.*;

public class decrypt extends AppCompatActivity{
    Button button3;
    Button button4;
    EditText content;
    EditText key;
    int keyindex = 0;
    int int_my_key;
    int int_my_content;
    int int_decryption;
    String temp_key = "abcd";
    String strdecryption;
    char decryption[] = new char[100];
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
    public void my_decrypt(String my_content,String my_key){
        for(int i = 0;i < my_content.length();i = i + 1) {
            int_my_content = (int)my_content.charAt(i);
            int_my_key = (int)my_key.charAt(keyindex);
            int_decryption = (int_my_content - 32) + 96 - int_my_key;   //解密的公式
            if(int_decryption < 32)
                int_decryption  = int_decryption + 96;
            decryption[i] = (char)int_decryption;
            keyindex = keyindex + 1;
            if(keyindex == my_key.length()) {
                keyindex = 0;
            }
        }
        keyindex = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(decrypt.this);    //跳出訊息表示明文
        builder.setTitle("After decryption : ");
        strdecryption = String.valueOf(decryption);
        builder.setMessage(strdecryption);
        builder.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decrypt);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        content = (EditText)findViewById(R.id.editText2);
        key = (EditText)findViewById(R.id.editText3);
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
                    if("".equals(key.getText().toString().trim())) {    //如果KEY的輸入欄位是空白的  則用檔案抓KEY出來解密
                        try {
                            get_key();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String my_content = content.getText().toString();
                        String my_key = temp_key;
                        my_decrypt(my_content, my_key);
                    }
                    else{       //反過來KEY的欄位有輸入東西則用輸入的KEY作解密
                        String my_content = content.getText().toString();
                        String my_key = key.getText().toString();
                        my_decrypt(my_content,my_key);
                    }
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
}
