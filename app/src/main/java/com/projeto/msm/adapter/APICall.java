package com.projeto.msm.adapter;

import com.google.gson.JsonObject;
import com.projeto.msm.model.AreaFrigorifica;
import com.projeto.msm.model.JWTToken;
import com.projeto.msm.model.Temperatura;
import com.projeto.msm.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APICall {

    String Base_URL ="http://msm.hopto.org";

    @Headers("Content-Type: application/json")
    @POST("/login")
    Call<User> login(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("area/frigorifica")
    Call<String> areafrig(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("area/frigorifica/temperatura")
    Call<String> areafrigtemp(@Body String body);

    @Headers("Content-Type: application/json")
    @PUT("limpeza/frigorifica/{id}")
    Call<String> areafriglimpeza(@Path ("id") String id, @Body String body);

    @Headers("Content-Type: application/json")
    @GET("area/frigorifica")
    Call<ArrayList<AreaFrigorifica>> areafrigtemp();

    @Headers("Content-Type: application/json")
    @GET("area/frigorifica/user/{id}")
    Call<ArrayList<Temperatura>> areafrigtempByuser(@Path ("id") String id);

    /*
    //just a test
    @Headers("Content-Type: application/json")
    @GET("ggg")
    Call<List<User>> getAllUsers(
            @Field("xyz") String field1
    );
    */

}
