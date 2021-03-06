package com.projeto.msm.adapter;

import com.google.gson.JsonObject;
import com.projeto.msm.model.Area;
import com.projeto.msm.model.AreaFrigorifica;
import com.projeto.msm.model.Componente;
import com.projeto.msm.model.ComponentesLimpos;
import com.projeto.msm.model.JWTToken;
import com.projeto.msm.model.Produto;
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
    Call<String> areafrig(@Header("Authorization") String authHeader, @Body String body);

    @Headers("Content-Type: application/json")
    @POST("area/frigorifica/temperatura")
    Call<String> areafrigtempsend(@Header("Authorization") String authHeader, @Body String body);

    @Headers("Content-Type: application/json")
    @PUT("limpeza/frigorifica/{id}")
    Call<String> areafriglimpeza(@Header("Authorization") String authHeader, @Path ("id") String id, @Body String body);

    @Headers("Content-Type: application/json")
    @GET("area/frigorifica")
    Call<ArrayList<AreaFrigorifica>> areafrigtemp(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @GET("area/frigorifica/user/{id}")
    Call<ArrayList<Temperatura>> areafrigtempByuser(@Path ("id") String id, @Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @POST("/rastreabilidade")
    Call<String> sendRastreabilidade(@Header("Authorization") String authHeader, @Body String body);

    @Headers("Content-Type: application/json")
    @GET("/area")
    Call<ArrayList<Area>> getAreas(@Header("Authorization") String authHeader);

    @Headers("Content-Type: application/json")
    @GET("/area/componentes/{area_num}")
    Call<ArrayList<Componente>> getComponentesArea(@Header("Authorization") String authHeader, @Path ("area_num") String area_num);

    @Headers("Content-Type: application/json")
    @POST("/area/componentes/{id}")
    Call<String> putLimpezaComponentesArea(@Header("Authorization") String authHeader, @Path ("id") String id, @Body String body);

    @Headers("Content-Type: application/json")
    @GET("/produto/validade/{ean}")
    Call<ArrayList<Produto>> getProdutoByEAN(@Header("Authorization") String authHeader, @Path ("ean") String ean);

    @Headers("Content-Type: application/json")
    @POST("/produto/validade")
    Call<String> putValidadeByProduto(@Header("Authorization") String authHeader, @Body String body);

    @Headers("Content-Type: application/json")
    @GET("area/componetes/user/{id}")
    Call<ArrayList<ComponentesLimpos>> getUserLastInputsCompoentes(@Path ("id") String id, @Header("Authorization") String authHeader);

    /*
    //just a test
    @Headers("Content-Type: application/json")
    @GET("ggg")
    Call<List<User>> getAllUsers(
            @Field("xyz") String field1
    );
    */

}
