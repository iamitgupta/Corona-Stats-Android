package com.corona.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BingApiRetrofitInstance {
    private static Retrofit retrofit = null;

    public static BingAPIService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl("https://bing.com/covid/")
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(BingAPIService.class);
    }
}