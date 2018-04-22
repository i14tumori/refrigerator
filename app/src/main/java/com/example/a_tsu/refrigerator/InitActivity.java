package com.example.a_tsu.refrigerator;

import android.app.Activity;
import android.os.*;
import java.util.Timer;
import java.util.TimerTask;
import android.content.*;

public class InitActivity extends Activity {
    Timer timer = new Timer();
    Handler handle = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        //画面を3秒表示する
        timer.schedule(new DownTime(),3000);
    }

    //時間経過で実行
    class DownTime extends TimerTask {
        @Override
        public void run() {
            handle.post(new Runnable() {
                @Override
                public void run() {
                    //画面を遷移させる
                    Intent intent = new Intent(getApplication(),HomeActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}



////////////////////////////////////////////////////////////////////////////
//スマホ側の戻るボタン(?)を押すと前の画面に戻るのどうにかして防ぎたい　　 //
//Initの画面まで戻ると画面遷移しないしボタンもないしでどうにもできなくなる//
//ファイルを一つにまとめたらいい ?　　　　　　　　　　　　　　　　　　　　//
//Fragmentで画面遷移ができるのならその方がいい                            //
//画面下部のボタン郡と画面遷移が汚い                                      //
////////////////////////////////////////////////////////////////////////////