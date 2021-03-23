package com.projeto.msm.adapter;

import com.google.gson.JsonObject;
import com.projeto.msm.model.JWTToken;
import com.projeto.msm.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APICall {

    String Base_URL ="http://msm.hopto.org";

    @Headers("Content-Type: application/json")
    @POST("/MSMAPI/api/login.php")
    Call<String> login(@Body String body);
}
