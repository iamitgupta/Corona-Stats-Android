package com.corona.services.covid19;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CovidApiRetrofitInstance {
    private static Retrofit retrofit = null;

    public static CovidAPIService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl("https://api.covid19api.com/")
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(CovidAPIService.class);
    }
}