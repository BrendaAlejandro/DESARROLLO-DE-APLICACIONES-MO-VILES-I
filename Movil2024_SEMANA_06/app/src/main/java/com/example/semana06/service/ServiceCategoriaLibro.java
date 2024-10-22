package com.example.semana06.service;

import com.example.semana06.Entity.Categoria;
import com.example.semana06.Entity.Pais;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceCategoriaLibro {
    @GET("servicio/util/listaCategoriaDeLibro")
    public Call<List<Categoria>> listaTodos();
}