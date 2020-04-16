package com.corona.services.covid19;

import com.corona.beans.CovidCountry;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface CovidAPIService {
    @GET
    Call<List<CovidCountry>> getCovidCountry(@Url String url);



}
