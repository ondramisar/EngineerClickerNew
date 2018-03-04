package com.companybest.ondra.engineerclickernew.networkAndLoading;

import com.companybest.ondra.engineerclickernew.models.DefaultMachine;
import com.companybest.ondra.engineerclickernew.models.Machine;
import com.companybest.ondra.engineerclickernew.models.Material;
import com.companybest.ondra.engineerclickernew.models.Worker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    @GET("defaultMachines")
    Call<List<DefaultMachine>> getMachines();

    @GET("workers")
    Call<List<Worker>> getWorkers();

    @GET("materials")
    Call<List<Material>> getMaterials();

    @GET("userMachine")
    Call<List<Machine>> getUserMachines();

    @POST("mobile-protocol-v2/contacts/message")
    @Headers("Content-Type: application/json")
    Call<DefaultMachine> feedback(@Body DefaultMachine feedback);

}