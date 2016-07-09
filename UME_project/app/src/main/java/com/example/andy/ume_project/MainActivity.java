package com.example.andy.ume_project;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.*;
public class MainActivity extends AppCompatActivity {
    Button button1;
    EditText key;
    int times = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button)findViewById(R.id.button);
        key = (EditText) findViewById(R.id.editText);
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                times = times + 1;
                try {
                    toNext();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Your key is : ");
                builder.setMessage(key.getText());
                builder.show();
            }
        });
    }
    public void toNext() throws IOException {
        String stringtimes = Integer.toString(times);
        FileWriter fw1 = new FileWriter("key");
        FileWriter fw2 = new FileWriter("time");
        fw1.write(key.getText().toString());
        fw2.write(stringtimes);
        fw1.close();
        fw2.close();
    }
}


