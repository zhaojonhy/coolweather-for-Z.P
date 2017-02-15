package com.zhaojonhy.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/2/15.
 */
public class Now {
    /**
     * cond : {"code":"100","txt":"晴"}
     * fl : 12
     * hum : 61
     * pcpn : 0
     * pres : 1031
     * tmp : 10
     * vis : 7
     * wind : {"deg":"340","dir":"西北风","sc":"4-5","spd":"21"}
     */

    //天气状况
    private CondBean cond;
    //体感温度
    @SerializedName("fl")
    private String feelingTmp;
    //相对湿度
    @SerializedName("hum")
    private String humidity;
    //降水量
    @SerializedName("pcpn")
    private String precipitation;
    //气压
    @SerializedName("pres")
    private String pressure;
    //温度
    @SerializedName("tmp")
    private String temperature;
    //能见度
    @SerializedName("vis")
    private String airVisibility;
    //风力情况
    private WindBean wind;

    public CondBean getCond() {
        return cond;
    }

    public void setCond(CondBean cond) {
        this.cond = cond;
    }

    public String getFeelingTmp() {
        return feelingTmp;
    }

    public void setFeelingTmp(String feelingTmp) {
        this.feelingTmp = feelingTmp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
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

    public String getAirVisibility() {
        return airVisibility;
    }

    public void setAirVisibility(String airVisibility) {
        this.airVisibility = airVisibility;
    }

    public WindBean getWind() {
        return wind;
    }

    public void setWind(WindBean wind) {
        this.wind = wind;
    }

    public static class CondBean {
        /**
         * code : 100
         * txt : 晴
         */

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

    public static class WindBean {
        /**
         * deg : 340
         * dir : 西北风
         * sc : 4-5
         * spd : 21
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

