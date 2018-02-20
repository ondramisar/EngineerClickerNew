package com.companybest.ondra.engineerclickernew.networkAndLoading;


import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ApiCallback<T> implements Callback<T> {

    @Override
    public void onResponse(@NonNull final Call<T> call, @NonNull final Response<T> response) {
        if (!response.isSuccessful()) {
            try {
                onFailure(call, new Exception(response.code() + " : " + response.errorBody().source().readUtf8()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (response.body() == null)
            onFailure(call, new Exception("No response"));
        else {
            Log.v("ApiCallback", call.toString() + " succ");
            onSuccess(call, response);
        }

    }

    public abstract void onSuccess(Call<T> call, Response<T> response);

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.v("Usern", call.request().toString() + " " + (t.getMessage() != null ? t.getMessage() : ""));

    }
}

