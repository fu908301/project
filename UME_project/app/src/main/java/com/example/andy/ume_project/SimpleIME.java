package com.example.andy.ume_project;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.UME.andy.ume_project.R;

import java.io.*;
import java.text.SimpleDateFormat;
/**
 * Created by Andy on 2016/5/17.
 */

public class SimpleIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    int tempindex;
    int index = 0;
    private KeyboardView kv;
    private Keyboard keyboard;
    private boolean caps = false;
    private boolean encrypt = false;
    String tempkey = "asdf";
    String mykey = "asdf";
    public void receive() throws IOException {  //利用開檔去讀取加密的KEY
        char buffer1[] = new char[1024];
        char buffer2[] = new char[1024];
        FileReader fr1;
        FileReader fr2;
        FileWriter fw1;
        File path = Environment.getExternalStorageDirectory();
        File file1 = new File(path, "key");
        File file2 = new File(path, "checkindex");
        try {
            fr1 = new FileReader(file2);
            int len1 = fr1.read(buffer1);
            String sindex = new String(buffer1,0,len1);
            tempindex = Integer.parseInt(sindex);
            fr1.close();
            fw1 = new FileWriter(file2,false);  //把checkindex抓出來之後  檔案裡面的checkindex放回0
            String stringindex = Integer.toString(0);
            fw1.write(stringindex);
            fw1.close();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
        try {
            fr2 = new FileReader(file1);
            int len2 = fr2.read(buffer2);
            tempkey = new String(buffer2,0,len2);
            fr2.close();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case -8763:     //按下CH鍵做的事情
                encrypt = !encrypt;
                try {
                    receive();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mykey = tempkey;
                if(tempindex == 1)      //如果有換過KEY的話 index歸零
                    index = 0;
                break;
            case -87:       // 1/2的鍵值
                kv.setKeyboard(new Keyboard(this, R.xml.qwerty2));
                break;
            case -76:       // 2/2的鍵值
                kv.setKeyboard(keyboard);
                break;
            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                if(encrypt){    //如果有按下CH鍵 則做加密
                    int test;
                    test = (int)code;
                    code = myencrypt(test);
                }
                ic.commitText(String.valueOf(code),1);
        }
    }

    public char myencrypt(int input) {
        char returnchar;
        int intkey =(int)mykey.charAt(index);
        input = (input +intkey) % 96 + 32;//96 = 128 - 32
        index++;
        if(index == mykey.length())
            index = 0;
        returnchar = (char)input;
        return returnchar;
    }
    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }
    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }
    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }
}