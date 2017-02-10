package com.zhaojonhy.coolweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/2/10.
 */
public class HttpUtil {
    //利用okHttp和服务器进行交互，这里我们提供一个回调和地址来处理服务器
    public static void sendOkHttpRequest(String address, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient() ;
        Request request = new Request.Builder().url(address).build() ;
        okHttpClient.newCall(request).enqueue(callback);
    }
}
