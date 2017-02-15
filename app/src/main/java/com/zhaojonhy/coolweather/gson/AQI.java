package com.zhaojonhy.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/2/15.
 */
public class AQI {
    /**
     * city : {"aqi":"54","pm10":"55","pm25":"33","qlty":"良"}
     */

    private AQICity city;

    public AQICity getCity() {
        return city;
    }

    public void setCity(AQICity city) {
        this.city = city;
    }

    public static class AQICity {
        /**
         * aqi : 54
         * pm10 : 55
         * pm25 : 33
         * quality : 良
         */

        private String aqi;
        private String pm10;
        private String pm25;
        //空气质量
        @SerializedName("qlty")
        private String quality;

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getPm10() {
            return pm10;
        }

        public void setPm10(String pm10) {
            this.pm10 = pm10;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }
    }
}
