package com.zhaojonhy.coolweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activi_weather);
        //初始化各个控件
        initView() ;
        //判断是否第一次进入此天气页面

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
    private void requestWeather(String weatherId) {

        String weatherUrl = API_SERVER
                +"city="+weatherId
                +"&key="+APP_KEY ;

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
                        }
                    }
                });
            }
        });
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

}
