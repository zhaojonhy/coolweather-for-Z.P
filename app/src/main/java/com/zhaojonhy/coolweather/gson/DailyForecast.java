package com.zhaojonhy.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/2/15.
 */
public class DailyForecast {
    /**
     * astro : {"mr":"21:55","ms":"09:17","sr":"06:38","ss":"17:56"}
     * cond : {"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"}
     * date : 2017-02-15
     * hum : 69
     * pcpn : 0.0
     * pop : 0
     * pres : 1028
     * tmp : {"max":"19","min":"7"}
     * uv : 8
     * vis : 10
     * wind : {"deg":"93","dir":"无持续风向","sc":"微风","spd":"6"}
     */

    //天文指数
    private Astro astro;
    //天气状况
    private CondBeanX cond;
    //日期
    private String date;
    //相对湿度
    @SerializedName("hum")
    private String humidity;
    //降水量
    @SerializedName("pcpn")
    private String precipitation;
    //降水概率
    private String pop;
    //气压
    @SerializedName("pres")
    private String pressure;
    //温度
    @SerializedName("tmp")
    private Temperature temperature;
    //紫外线指数
    private String uv;
    //能见度
    @SerializedName("vis")
    private String airVisibility;
    //风力情况
    private WindBeanX wind;

    public Astro getAstro() {
        return astro;
    }

    public void setAstro(Astro astro) {
        this.astro = astro;
    }

    public CondBeanX getCond() {
        return cond;
    }

    public void setCond(CondBeanX cond) {
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

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
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

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getAirVisibility() {
        return airVisibility;
    }

    public void setAirVisibility(String airVisibility) {
        this.airVisibility = airVisibility;
    }

    public WindBeanX getWind() {
        return wind;
    }

    public void setWind(WindBeanX wind) {
        this.wind = wind;
    }

    public static class Astro {
        /**
         * mr : 21:55
         * ms : 09:17
         * sr : 06:38
         * ss : 17:56
         */

        /*
        * mr	月升时间
        * sr	日出时间
        * ss	日落时间
        * ms	月落时间
        * */

        private String mr;
        private String ms;
        private String sr;
        private String ss;

        public String getMr() {
            return mr;
        }

        public void setMr(String mr) {
            this.mr = mr;
        }

        public String getMs() {
            return ms;
        }

        public void setMs(String ms) {
            this.ms = ms;
        }

        public String getSr() {
            return sr;
        }

        public void setSr(String sr) {
            this.sr = sr;
        }

        public String getSs() {
            return ss;
        }

        public void setSs(String ss) {
            this.ss = ss;
        }
    }

    public static class CondBeanX {
        /**
         * code_d : 100
         * code_n : 100
         * txt_d : 晴
         * txt_n : 晴
         */

        //白天天气状况代码
        private String code_d;
        //夜间天气状况代码
        private String code_n;
        //白天天气状况代码
        private String txt_d;
        //夜间天气状况描述
        private String txt_n;

        public String getCode_d() {
            return code_d;
        }

        public void setCode_d(String code_d) {
            this.code_d = code_d;
        }

        public String getCode_n() {
            return code_n;
        }

        public void setCode_n(String code_n) {
            this.code_n = code_n;
        }

        public String getTxt_d() {
            return txt_d;
        }

        public void setTxt_d(String txt_d) {
            this.txt_d = txt_d;
        }

        public String getTxt_n() {
            return txt_n;
        }

        public void setTxt_n(String txt_n) {
            this.txt_n = txt_n;
        }
    }

    public static class Temperature {
        /**
         * max : 19
         * min : 7
         */

        private String max;
        private String min;

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }
    }

    public static class WindBeanX {
        /**
         * deg : 93
         * dir : 无持续风向
         * sc : 微风
         * spd : 6
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

