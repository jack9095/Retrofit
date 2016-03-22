package com.jx.retrofitlearn;


import com.jx.retrofitlearn.converter.gson.GsonConverterFactory;
import com.jx.retrofitlearn.converter.string.StringConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by fei.wang on 2015/11/7.
 *
 * 服务产生类
 */
public class ServiceGenerator {
    public static final String API_BASE_URL = "http://192.168.0.106/";
    public static final String API_BASE_URL2 = "http://192.168.6.134/";
    public static OkHttpClient sOkHttpClient = new OkHttpClient(); // OkHttp源码中的

    // 创建请求
    private static Retrofit.Builder mBuilder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL2)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit.Builder mStringBuilder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL2)
            .addConverterFactory(StringConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = mBuilder.client(sOkHttpClient).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService2(Class<S> serviceClass) {
        Retrofit retrofit = mStringBuilder.client(sOkHttpClient).build();
        return retrofit.create(serviceClass);
    }
}
