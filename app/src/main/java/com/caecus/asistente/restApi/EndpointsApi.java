package com.caecus.asistente.restApi;


import com.caecus.asistente.Entidades.Aviso;
import com.caecus.asistente.restApi.model.TokenResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface EndpointsApi {


    @Headers( "Content-Type: application/json" )
    @POST(ConstantesRestApi.LOGIN)
    Call<TokenResponse> login(@Body Map<String, String> body);


    @Headers( "Content-Type: application/json" )
    @POST(ConstantesRestApi.REGISTER)
    Call<TokenResponse> register(@Body Map<String, String> body);

    @Headers("Content-Type: application/json")
    @POST(ConstantesRestApi.REGISTERPDV)
    Call<TokenResponse> registerPDV(@Body Map<String, String> body, @Header("Authorization") String bearer);

    @Headers("Content-Type: application/json")
    @POST(ConstantesRestApi.LOGOUT)
    Call<TokenResponse> logout(@Header("Authorization") String bearer);

    @Headers( "Content-Type: application/json" )
    @POST(ConstantesRestApi.EMPAREJARPDV)
    Call<TokenResponse> emparejarPDV(@Body Map<String, String> body, @Header("Authorization") String bearer);

    @Headers( "Content-Type: application/json" )
    @POST(ConstantesRestApi.RECIVIRAVISO)
    Call<Aviso> recibirAviso(@Body Map<String, String> body);

}
