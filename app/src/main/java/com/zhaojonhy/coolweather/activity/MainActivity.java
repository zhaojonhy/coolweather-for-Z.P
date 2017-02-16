package com.zhaojonhy.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.zhaojonhy.coolweather.R;
import com.zhaojonhy.coolweather.gson.Weather;

public class MainActivity extends Activity {
    private final static String TAG = MainActivity.class.getSimpleName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //判断是否第一次进入程序
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this) ;
        String weather = sp.getString("weather",null) ;
        //如果不是第一次进入则跳转到天气界面（没有则返回null）
        if( weather != null ){
            Intent intent = new Intent(this, Weather.class) ;
            startActivity(intent) ;
            finish() ;
        }
    }
}
