package com.companybest.ondra.engineerclickernew.networkAndLoading;

import com.companybest.ondra.engineerclickernew.firebasePostModels.UserPost;
import com.companybest.ondra.engineerclickernew.models.DefaultMachine;
import com.companybest.ondra.engineerclickernew.models.DefaultMaterial;
import com.companybest.ondra.engineerclickernew.models.DefaultWorker;
import com.companybest.ondra.engineerclickernew.models.UserMachine;
import com.companybest.ondra.engineerclickernew.models.User;
import com.companybest.ondra.engineerclickernew.models.UserMaterial;
import com.companybest.ondra.engineerclickernew.models.UserWorker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Api {

    @GET("defaultMachines")
    Call<List<DefaultMachine>> getMachines();

    @GET
    Call<List<UserMachine>> getUserMachines(@Url String url);

    @GET("defaultWorkers")
    Call<List<DefaultWorker>> getDefaultWorkers();

    @GET
    Call<List<UserWorker>> getUserWorkers(@Url String url);

    @GET("defaultMaterials")
    Call<List<DefaultMaterial>> getDefaultMaterials();

    @GET
    Call<List<UserMaterial>> getUserMaterials(@Url String url);

    @GET
    Call<User> getUser(@Url String url);

    @POST("user")
    Call<String> postUser(@Body UserPost userPost);



    @POST("mobile-protocol-v2/contacts/message")
    @Headers("Content-Type: application/json")
    Call<DefaultMachine> feedback(@Body DefaultMachine feedback);

}