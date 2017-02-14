package com.zhaojonhy.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.zhaojonhy.coolweather.db.City;
import com.zhaojonhy.coolweather.db.County;
import com.zhaojonhy.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/2/13.
 */
public class Uitlity {
    private final static String TAG = Uitlity.class.getSimpleName() ;
/*
*解析和处理服务器返回的省级数据
* */
    public static boolean handleProvinceResponse(String response){
        //如果数据不为空则解析json数据并且返回true
        Log.d(TAG,"response:" + response) ;
        if( !TextUtils.isEmpty(response) ){
            Log.d(TAG,"response:"+" isEmpty" + "yes") ;
            try{
                Log.d(TAG,"response:"+" try" + "yes") ;
                JSONArray allProvinceJson = new JSONArray(response) ;
                //遍历整个json数据
                for (int i = 0 ; i < allProvinceJson.length() ; i++ ) {
                    //解析数据
                    JSONObject provinceObject = allProvinceJson.getJSONObject(i) ;
                    //把数据放入数据库中（之前微博是放入到集合中）
                    Province province = new Province() ;
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    //用save()存储数据库
                    province.save() ;
                    Log.d(TAG,"response:"+" province.save()" + "yes") ;
                }
                Log.d(TAG,"response:"+" out try" + "yes") ;
                return true ;
            }catch (JSONException e){
                Log.d(TAG,"response:"+" JSONException" + e.getMessage()) ;
                e.printStackTrace();
            }
        }
        Log.d(TAG,"response:"+" out isEmpty" + "yes") ;
        return false ;
    }

    /*
    *解析和处理服务器返回的市级数据
    * */
    public static boolean handleCityResponse(String response,int privinceId){
        //如果数据不为空则解析json数据并且返回true
        if( !TextUtils.isEmpty(response) ){
            try{
                JSONArray allCityJson = new JSONArray(response) ;
                //遍历整个json数据
                for (int i = 0 ; i < allCityJson.length() ; i++ ) {
                    //解析数据
                    JSONObject cityObject = allCityJson.getJSONObject(i) ;
                    //把数据放入数据库中（之前微博是放入到集合中）
                    City city = new City() ;
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(privinceId);
                    //用save()存储数据库
                    city.save() ;
                }
                return true ;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false ;
    }


    /*
    *解析和处理服务器返回的县级数据
    * */
    public static boolean handleCountyResponse(String response,int cityId){
        //如果数据不为空则解析json数据并且返回true
        if( !TextUtils.isEmpty(response) ){
            try{
                JSONArray allCountyJson = new JSONArray(response) ;
                //遍历整个json数据
                for (int i = 0 ; i < allCountyJson.length() ; i++ ) {
                    //解析数据
                    JSONObject countyObject = allCountyJson.getJSONObject(i) ;
                    //把数据放入数据库中（之前微博是放入到集合中）
                    County county = new County() ;
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    //用save()存储数据库
                    county.save() ;
                }
                return true ;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false ;
    }

}

