package com.companybest.ondra.engineerclickernew.networkAndLoading;

import com.companybest.ondra.engineerclickernew.models.Machine;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    @GET("machines")
    Call<List<Machine>> getMachines();

    @POST("mobile-protocol-v2/contacts/message")
    @Headers("Content-Type: application/json")
    Call<Machine> feedback(@Body Machine feedback);

}