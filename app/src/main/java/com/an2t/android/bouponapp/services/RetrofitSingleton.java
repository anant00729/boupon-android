package com.an2t.android.bouponapp.services;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anantawasthy on 3/24/17.
 */

public class RetrofitSingleton {


    // for main inner api
//    public static final String BASE_URL_INNER_API = "http://ec2-54-255-228-74.ap-southeast-1.compute.amazonaws.com:3000/mobile/";

    // for Images
    public static final String BASE_IMAGE_URL = "http://ec2-54-255-228-74.ap-southeast-1.compute.amazonaws.com:3000";

    // for main inner api for test
//    public static final String TEST_URL_INNER_API = "http://ec2-54-179-133-139.ap-southeast-1.compute.amazonaws.com:5000/mobile/";

    public static final String BASE_URL = "https://movie-star-60.herokuapp.com/boupon/v1/";


    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(50000, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build();

        if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

        }
        return retrofit;
    }
}
