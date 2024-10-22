package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Sala;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceSala {
    @GET("servicio/sala/porNumero/{numero}")
    public Call<List<Sala>> listaPorNumero(@Path("numero")String numero);
    @POST("servicio/sala")
    public Call<Sala> Registra(@Body Sala objSala);
    @GET("servicio/sala/porNumero/{numero}")
    public Call<List<Sala>> listaPorNumeroIgual(@Path("numero")String numero);
    @PUT("servicio/sala")
    public Call<Sala> actualizaSala(@Body Sala obj);
}