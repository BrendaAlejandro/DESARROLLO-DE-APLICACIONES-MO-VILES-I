package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Revista;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceRevista {

    @GET("servicio/revista/porNombre/{nombre}")
    public Call<List<Revista>> listaPorNombre(@Path("nombre")String nombre);

    @POST("servicio/revista")
    public Call<Revista> registraRevista(@Body Revista objRevista);


    @PUT("servicio/revista")
    public Call<Revista> actualizaRevista(@Body Revista objRevista);

    @GET("servicio/revista/porNombreIgual/{nombre}")
    public Call<List<Revista>> listaRevistaPorNombreIgual(@Path("nombre")String nombre);
}
