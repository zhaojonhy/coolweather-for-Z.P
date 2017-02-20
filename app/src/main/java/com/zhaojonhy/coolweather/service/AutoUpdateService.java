package com.zhaojonhy.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.zhaojonhy.coolweather.gson.Weather;
import com.zhaojonhy.coolweather.util.Constants;
import com.zhaojonhy.coolweather.util.HttpUtil;
import com.zhaojonhy.coolweather.util.Uitlity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/20.
 */

/*
*  更新的做法就是把更行下来获取的数据放入SharedPreferences中去，
*  这样每次打开weather就是最新的数据了
* */
public class AutoUpdateService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //更新天气信息
        updateWearther() ;
        //更新每日一图
        updateBingPic() ;
        //定时更新。运用Alarm机制 ，我们要做成让定时任务的时间从系统开机时算起
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE) ;
        //设置定时的毫秒数
        int anHour = 6 * 60 * 60 * 1000 ;//6小时的毫秒数
        //把定时服务的时间设置成从开机时候算起
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour ;
        //PendingIntent设定定时任务的对象,绑定了闹钟的执行动作
        Intent i = new Intent(this,AutoUpdateService.class) ;
        PendingIntent pi = PendingIntent.getService(this,0,i,0) ;
        //取消定时服务
        manager.cancel(pi);
        //绑定定时服务
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /*
    *  更新天气信息
    * */
    private void updateWearther() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this) ;
        String weatherString = sharedPreferences.getString("weather",null) ;
        //这个是地址id的缓存
        if( weatherString != null ){
            //有缓存时直接解析天气数据,不是从三级地址中选择id，所以得从缓存中选在天气位置的地址
            Weather weather = Uitlity.handleWeatherResponse(weatherString) ;
            String weatherId = weather.getBasic().getId() ;
            String weatherUrl = Constants.API_SERVER
                    +"city="+weatherId
                    +"&"+Constants.APP_KEY ;
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string() ;
                    Weather weather = Uitlity.handleWeatherResponse(responseText) ;
                    if( weather != null && "ok".equals(weather.getStatus())){
                        SharedPreferences.Editor editor = PreferenceManager.
                                getDefaultSharedPreferences(AutoUpdateService.this).
                                edit() ;
                        editor.putString("weather",responseText) ;
                        editor.apply();
                    }
                }
            });
        }
    }

    /*
    *  更新必应每日一图
    * */
    private void updateBingPic() {
        String requestBingPIc = "http://guolin.tech/api/bing_pic" ;
        HttpUtil.sendOkHttpRequest(requestBingPIc, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string() ;
                //储存地址
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(AutoUpdateService.this).
                        edit() ;
                editor.putString("bing_pic",bingPic) ;
                editor.apply() ;
            }
        });
    }
}
