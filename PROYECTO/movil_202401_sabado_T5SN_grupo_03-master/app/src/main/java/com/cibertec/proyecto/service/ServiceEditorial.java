package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Editorial;
import com.cibertec.proyecto.entity.Revista;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceEditorial {
    @GET("servicio/editorial/porNombre/{nombre}")
    public Call<List<Editorial>> listaPorNombre(@Path("nombre")String nombre);

    //REGISTRAR
    @POST("servicio/editorial")
    public Call<Editorial> registra(@Body Editorial objEditorial);

    @GET("servicio/editorial/porNombreIgual/{nombre}")
    public Call<List<Editorial>> porNombreIgual(@Path("nombre")String nombre);

    @PUT("servicio/editorial")
    public Call<Editorial> actualizaEditorial(@Body Editorial obj);

}