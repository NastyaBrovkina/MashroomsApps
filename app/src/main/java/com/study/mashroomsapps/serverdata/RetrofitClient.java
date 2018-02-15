package com.study.mashroomsapps.serverdata;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nvv on 03.02.2018.
 */

public class RetrofitClient {

    //need server adress with port for test use local adress
    String API_BASE_URL = "http://192.168.0.100:8080/";
    Retrofit retrofit = null;

    public RetrofitClient() {
        init();
    }

    private void init(){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );


        retrofit = builder
                        .client(
                                httpClient.build()
                        )
                        .build();
    }
}
