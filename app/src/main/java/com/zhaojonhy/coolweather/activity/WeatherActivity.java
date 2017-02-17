package com.zhaojonhy.coolweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhaojonhy.coolweather.R;
import com.zhaojonhy.coolweather.gson.DailyForecast;
import com.zhaojonhy.coolweather.gson.Weather;
import com.zhaojonhy.coolweather.util.HttpUtil;
import com.zhaojonhy.coolweather.util.Uitlity;

import java.io.IOException;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/16.
 */
public class WeatherActivity extends Activity {

    private final static String TAG = WeatherActivity.class.getSimpleName() ;
    private final static String API_SERVER = "https://free-api.heweather.com/v5/weather?" ;
    private final static String APP_KEY = "key=3937d0e7db0c4458ba147dd466f96ed2" ;

    //可下滑布局
    private ScrollView weatherLayout ;
    //显示城市和更新时间
    private TextView titleCity ;
    private TextView titleUpdataTime ;
    //显示温度跟天气情况
    private TextView degreeText ;
    private TextView weatherInfoText ;
    //未来几天天气预报，动态添加
    private LinearLayout forecastLayout ;
    //空气质量
    private TextView aqiText ;
    private TextView pm25Text ;
    //生活建议
    private TextView comfortText ;
    private TextView carWashText ;
    private TextView sportText ;
    //必应背景图
    private ImageView bingPicImg ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将背景图和状态栏融合在一起
        decorStatusBar() ;
        setContentView(R.layout.activi_weather);
        //初始化各个控件
        initView() ;
        //判断是否第一次进入此天气页面
        isFirstReadServicer() ;
        //判断是否要加载必应图，如果事先有缓存则加载，没有则去必应获取
        isloadBingPic() ;
    }

    private void initView(){
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout) ;
        titleCity = (TextView) findViewById(R.id.title_city) ;
        titleUpdataTime = (TextView) findViewById(R.id.title_updata_time) ;
        degreeText = (TextView) findViewById(R.id.degree_text) ;
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text) ;
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout) ;
        aqiText = (TextView) findViewById(R.id.qpi_text) ;
        pm25Text = (TextView) findViewById(R.id.pm25_text) ;
        comfortText = (TextView) findViewById(R.id.comfort_text) ;
        carWashText =  (TextView) findViewById(R.id.car_wash_text) ;
        sportText = (TextView) findViewById(R.id.sport_text) ;
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img) ;
    }

    private void isFirstReadServicer(){
        //读取本地缓存，如果是第一次进入则读取服务器，如果不是就读取本地缓存
        //在MainActivity中做过判断，下面缓存
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this) ;
        String weatherString = sp.getString("weather",null) ;//有则返回"weather",没有则返回null
        //判断"weather"是否有本地缓存
        if( weatherString != null ){
            //有缓存直接解析天气数据
            Weather weather = Uitlity.handleWeatherResponse(weatherString) ;
            showWeatherInfo(weather) ;
        }else {
            //没有缓存从服务器查询天气并储存
            String weatherId = getIntent().getStringExtra("weather_id") ;
            //显示滑动的控件，天气菜单
            weatherLayout.setVisibility(View.VISIBLE);
            requestWeather(weatherId) ;
        }
    }

    /*
    *  根据weatherId查询服务器上的天气信息，并存入本地
    * */
    private void requestWeather(final String weatherId) {

        String weatherUrl = API_SERVER
                +"city="+weatherId
                +"&"+APP_KEY ;

        Log.d(TAG,"weatherUrl " + weatherUrl ) ;

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //服务器获取失败
                e.printStackTrace();
                //在主线程中提示消息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",
                                Toast.LENGTH_SHORT).show() ;
                        Log.d(TAG,"failed " + "response失败" ) ;
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取天气信息成功
                final String responseText = response.body().string() ;
                final Weather weather = Uitlity.handleWeatherResponse(responseText) ;
                //主线程中进行UI操作
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //如果weather不为空且请求服务器成功（status为"ok"），则进行UI操作
                        Log.d(TAG,"weather.getStatus()" + weather.getStatus() ) ;
                        if( weather != null && "ok".equals(weather.getStatus()) ){
                            //用来写入存储的数据
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).
                                    edit();
                            //放入数据
                            editor.putString("weather",responseText) ;
                            editor.apply() ;
                            //展示实体类中的数据
                            showWeatherInfo(weather) ;
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",
                                    Toast.LENGTH_SHORT).show() ;
                            Log.d(TAG,"failed " + "failed" ) ;
                        }
                    }
                });
            }
        });

        //数据请求成功加载必应图片
        loadBingPic();
    }

    /*
    *  处理并展示weather实体类数据
    * */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.getBasic().getCity() ;
        //split()分割空格，去后一位的数据
        String updateTime = weather.getBasic().getUpdate().getUpdataTime().split(" ")[1] ;
        String degree = weather.getNow().getTemperature() + "℃" ;
        String weatherInfo = weather.getNow().getCond().getTxt() ;

        //设置城市、更新时间、温度、天气情况
        titleCity.setText(cityName) ;
        titleUpdataTime.setText(updateTime) ;
        degreeText.setText(degree) ;
        weatherInfoText.setText(weatherInfo) ;
        //动态绑定未来几天信息
        //先清空布局
        forecastLayout.removeAllViews();
        //动态添加
        for (DailyForecast forecast : weather.getDailyForecastList() ) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false) ;
            TextView dataText = (TextView) view.findViewById(R.id.data_text) ;
            TextView condInfoText = (TextView) view.findViewById(R.id.cond_info_text) ;
            TextView condMaxText = (TextView) view.findViewById(R.id.cond_max_text) ;
            TextView condMinText = (TextView) view.findViewById(R.id.cond_min_text) ;

            dataText.setText(forecast.getDate());
            condInfoText.setText(forecast.getCond().getTxt_d());
            condMaxText.setText(forecast.getTemperature().getMax() + "℃" );
            condMinText.setText(forecast.getTemperature().getMax() + "℃" );

            forecastLayout.addView(view);
        }

        //空气质量
        if( weather.getAqi() != null ){
            aqiText.setText( weather.getAqi().getCity().getAqi());
            pm25Text.setText( weather.getAqi().getCity().getPm25());
        }
        //生活建议
        String comfort = "舒适度: " + weather.getSuggestion().getComfort().getTxt() ;
        String carWash = "洗车指数: " + weather.getSuggestion().getCarWash().getTxt() ;
        String sport = "运动指数: " + weather.getSuggestion().getSport().getTxt() ;

        comfortText.setText(comfort) ;
        carWashText.setText(carWash) ;
        sportText.setText(sport) ;
        forecastLayout.setVisibility(View.VISIBLE);
    }


    private void isloadBingPic() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this) ;
        String bingPic = sp.getString("bing_pic",null) ;
        if( bingPic != null ){
            Glide.with(this).load(bingPic).into(bingPicImg) ; //图片加载库
        }else {
            //无缓存时请求网络
            loadBingPic();
        }
    }

    /*
    *  加载必应每日一图
    * */
    private void loadBingPic() {
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
                        getDefaultSharedPreferences(WeatherActivity.this).
                        edit() ;
                editor.putString("bing_pic",bingPic) ;
                editor.apply() ;
                //更新到主线程上
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg) ;
                    }
                });
            }
        });
    }

    /*
    * 将背景图和状态栏融合在一起
    * */
    private void decorStatusBar(){
        //这个功能要Android5.0以及以上的系统才有所以要判断版本号
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView() ;
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            |View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

}
