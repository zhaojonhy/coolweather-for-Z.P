package com.zhaojonhy.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/2/15.
 */
public class HourlyForecast {
    /**
     * cond : {"code":"100","txt":"晴"}
     * date : 2017-02-15 10:00
     * hum : 53
     * pop : 0
     * pres : 1030
     * tmp : 17
     * wind : {"deg":"150","dir":"东南风","sc":"微风","spd":"5"}
     */

    private CondBeanXX cond;
    private String date;
    //相对湿度
    @SerializedName("hum")
    private String humidity;
    //降水概率
    private String pop;
    //气压
    @SerializedName("pres")
    private String pressure;
    //温度
    @SerializedName("tmp")
    private String temperature;
    //风力情况
    private WindBeanXX wind;

    public CondBeanXX getCond() {
        return cond;
    }

    public void setCond(CondBeanXX cond) {
        this.cond = cond;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public WindBeanXX getWind() {
        return wind;
    }

    public void setWind(WindBeanXX wind) {
        this.wind = wind;
    }

    public static class CondBeanXX {
        /**
         * code : 100
         * txt : 晴
         */

        //天气状况代码、天气状况描述
        private String code;
        private String txt;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }
    }

    public static class WindBeanXX {
        /**
         * deg : 150
         * dir : 东南风
         * sc : 微风
         * spd : 5
         */

        //风向（360度）
        private String deg;
        //风向
        private String dir;
        //风力等级
        private String sc;
        //风速
        private String spd;

        public String getDeg() {
            return deg;
        }

        public void setDeg(String deg) {
            this.deg = deg;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public String getSc() {
            return sc;
        }

        public void setSc(String sc) {
            this.sc = sc;
        }

        public String getSpd() {
            return spd;
        }

        public void setSpd(String spd) {
            this.spd = spd;
        }
    }
}

