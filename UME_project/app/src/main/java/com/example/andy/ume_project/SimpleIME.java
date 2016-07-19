package com.example.andy.ume_project;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.os.Environment;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;

import java.io.*;

/**
 * Created by Andy on 2016/5/17.
 */

public class SimpleIME extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    int tempindex;
    int index = 0;
    private KeyboardView kv;
    private KeyboardView kv2;
    private Keyboard keyboard;
    private Keyboard keyboard2;
    private boolean caps = false;
    private boolean encrypt = false;
    String tempkey = "asdf";
    String mykey = "asdf";
    public void receive() throws IOException {
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
            fw1 = new FileWriter(file2,false);
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
            case -8763:
                encrypt = !encrypt;
                try {
                    receive();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mykey = tempkey;
                if(tempindex == 1)
                    index = 0;
                break;
            case -87:
                kv.setKeyboard(new Keyboard(this,R.xml.qwerty2));
                break;
            case -76:
                kv.setKeyboard(new Keyboard(this,R.xml.qwerty));
                break;
            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                if(encrypt){
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