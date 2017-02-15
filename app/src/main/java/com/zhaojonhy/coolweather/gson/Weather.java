package com.zhaojonhy.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/2/15.
 */
public class Weather {
    /**
     * aqi : {"city":{"aqi":"54","pm10":"55","pm25":"33","qlty":"良"}}
     * basic : {"city":"闽侯","cnty":"中国","id":"CN101230103","lat":"26.194000","lon":"119.142000","update":{"loc":"2017-02-15 09:52","utc":"2017-02-15 01:52"}}
     * daily_forecast : [{"astro":{"mr":"21:55","ms":"09:17","sr":"06:38","ss":"17:56"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2017-02-15","hum":"69","pcpn":"0.0","pop":"0","pres":"1028","tmp":{"max":"19","min":"7"},"uv":"8","vis":"10","wind":{"deg":"93","dir":"无持续风向","sc":"微风","spd":"6"}},{"astro":{"mr":"22:47","ms":"09:53","sr":"06:38","ss":"17:56"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2017-02-16","hum":"72","pcpn":"0.0","pop":"0","pres":"1023","tmp":{"max":"23","min":"9"},"uv":"8","vis":"10","wind":{"deg":"182","dir":"无持续风向","sc":"微风","spd":"3"}},{"astro":{"mr":"23:38","ms":"10:29","sr":"06:37","ss":"17:57"},"cond":{"code_d":"101","code_n":"101","txt_d":"多云","txt_n":"多云"},"date":"2017-02-17","hum":"64","pcpn":"0.0","pop":"0","pres":"1022","tmp":{"max":"24","min":"12"},"uv":"8","vis":"10","wind":{"deg":"139","dir":"无持续风向","sc":"微风","spd":"3"}}]
     * hourly_forecast : [{"cond":{"code":"100","txt":"晴"},"date":"2017-02-15 10:00","hum":"53","pop":"0","pres":"1030","tmp":"17","wind":{"deg":"150","dir":"东南风","sc":"微风","spd":"5"}},{"cond":{"code":"100","txt":"晴"},"date":"2017-02-15 13:00","hum":"44","pop":"0","pres":"1027","tmp":"22","wind":{"deg":"82","dir":"东风","sc":"微风","spd":"6"}},{"cond":{"code":"100","txt":"晴"},"date":"2017-02-15 16:00","hum":"52","pop":"0","pres":"1026","tmp":"23","wind":{"deg":"94","dir":"东风","sc":"微风","spd":"7"}},{"cond":{"code":"100","txt":"晴"},"date":"2017-02-15 19:00","hum":"77","pop":"0","pres":"1026","tmp":"21","wind":{"deg":"66","dir":"东北风","sc":"微风","spd":"5"}},{"cond":{"code":"100","txt":"晴"},"date":"2017-02-15 22:00","hum":"92","pop":"0","pres":"1026","tmp":"18","wind":{"deg":"18","dir":"东北风","sc":"微风","spd":"4"}}]
     * now : {"cond":{"code":"100","txt":"晴"},"fl":"12","hum":"61","pcpn":"0","pres":"1031","tmp":"10","vis":"7","wind":{"deg":"340","dir":"西北风","sc":"4-5","spd":"21"}}
     * status : ok
     * suggestion : {"air":{"brf":"良","txt":"气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。"},"comf":{"brf":"舒适","txt":"白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"},"cw":{"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"},"drsg":{"brf":"较舒适","txt":"建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"},"flu":{"brf":"易发","txt":"昼夜温差很大，易发生感冒，请注意适当增减衣服，加强自我防护避免感冒。"},"sport":{"brf":"较适宜","txt":"天气较好，但考虑气温较低，推荐您进行室内运动，若户外适当增减衣物并注意防晒。"},"trav":{"brf":"适宜","txt":"天气较好，温度适宜，是个好天气哦。这样的天气适宜旅游，您可以尽情地享受大自然的风光。"},"uv":{"brf":"中等","txt":"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"}}
     */

    private AQI aqi;
    private Basic basic;
    private Now now;
    private String status;
    private Suggestion suggestion;
    //注解映射
    @SerializedName("daily_forecast")
    private List<DailyForecast> dailyForecastList;
    @SerializedName("hourly_forecast")
    private List<HourlyForecast> hourlyForecastList;

    public AQI getAqi() {
        return aqi;
    }

    public void setAqi(AQI aqi) {
        this.aqi = aqi;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public Now getNow() {
        return now;
    }

    public void setNow(Now now) {
        this.now = now;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public List<DailyForecast> getDailyForecastList() {
        return dailyForecastList;
    }

    public void setDailyForecastList(List<DailyForecast> dailyForecastList) {
        this.dailyForecastList = dailyForecastList;
    }

    public List<HourlyForecast> getHourlyForecastList() {
        return hourlyForecastList;
    }

    public void setHourlyForecastList(List<HourlyForecast> hourlyForecastList) {
        this.hourlyForecastList = hourlyForecastList;
    }

}
