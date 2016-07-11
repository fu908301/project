package com.example.andy.ume_project;

/**
 * Created by Andy on 2016/7/11.
 */

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
public class decrypt extends AppCompatActivity{
    Button button3;
    Button button4;
    EditText content;
    EditText key;
    int keyindex = 0;
    int int_my_key;
    int int_my_content;
    int int_decryption;
    int temp;
    char decryption[] = new char[100];
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
                String my_content = content.getText().toString();
                String my_key = key.getText().toString();
                for(int i = 0;i < my_content.length();i = i + 1) {
                    int_my_content = (int)my_content.charAt(i);
                    int_my_key = (int)my_key.charAt(keyindex);
                    temp = int_my_content - int_my_key;
                    if(temp < 0)
                        int_decryption = 128 + temp;
                    else if(temp > 0)
                        int_decryption = temp % 128;
                    decryption[i] = (char)int_decryption;
                    keyindex = keyindex + 1;
                    if(keyindex == my_key.length()) {
                        keyindex = 0;
                    }
                }
                keyindex = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(decrypt.this);
                builder.setTitle("After decryption : ");
                builder.setMessage(decryption.toString());
                builder.show();
            }
        });
        button4.setOnClickListener(new Button.OnClickListener() {
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
