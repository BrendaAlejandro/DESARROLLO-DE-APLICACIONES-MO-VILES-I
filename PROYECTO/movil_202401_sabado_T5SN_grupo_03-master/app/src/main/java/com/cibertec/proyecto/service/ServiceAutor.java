package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Autor;
import com.cibertec.proyecto.entity.Libro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceAutor {

    @GET("servicio/autor/porNombre/{nombre}")
    public Call<List<Autor>> listaporNombre(@Path("nombre")String nombre);

    @POST("servicio/autor")
    public Call<Autor> registra(@Body Autor objAutor);

    @GET("servicio/autor/porNombreApellidoIgual/{nombre}-{apellidos}")
    public Call<List<Autor>> listaPorNombreApellidoIgual(@Path("nombre")String nombre, @Path("apellidos")String apellidos);

    @PUT("servicio/autor")
    public Call<Autor> actualizaAutor(@Body Autor obj);
}