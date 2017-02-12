package com.zhaojonhy.coolweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zhaojonhy.coolweather.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName() ;
    public static String COUNTRID =  "http://files.heweather.com/china-city-list.json" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpUtil.sendOkHttpRequest(COUNTRID, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"fail-->" + e.getMessage()) ;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG,"onResponse-->" + response ) ;
            }
        });
    }
}
