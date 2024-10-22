package com.example.semana06.service;


import com.example.semana06.Entity.Categoria;
import com.example.semana06.Entity.Libro;
import com.example.semana06.Entity.Pais;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceLibro {
    @POST("servicio/libro")
    public Call<Libro> Registra(@Body Libro objLibro);
}