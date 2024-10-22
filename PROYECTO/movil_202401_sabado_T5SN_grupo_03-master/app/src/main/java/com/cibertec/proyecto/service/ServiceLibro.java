package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Libro;
import com.cibertec.proyecto.entity.Sala;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceLibro {


    @GET("servicio/libro/porTitulo/{titulo}")
    public Call<List<Libro>> listaPorTitulo(@Path("titulo")String titulo);



}



