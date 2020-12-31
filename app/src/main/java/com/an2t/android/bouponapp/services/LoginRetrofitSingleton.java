package com.an2t.android.bouponapp.services;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anantawasthy on 3/24/17.
 */

public class LoginRetrofitSingleton {

    // for Main
    public static final String BASE_URL_FOR_AUTH = "http://ec2-54-255-228-74.ap-southeast-1.compute.amazonaws.com:3000/";

    // for Test
    public static final String TEST_URL_FOR_AUTH = "http://ec2-54-179-133-139.ap-southeast-1.compute.amazonaws.com:5000/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(50000, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL_FOR_AUTH)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
        return retrofit;
    }
}
