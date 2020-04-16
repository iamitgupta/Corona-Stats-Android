package com.corona.services;

import com.corona.beans.Report;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BingAPIService {
    @GET("data")
    Call<Report> getCoronaReports();


}
