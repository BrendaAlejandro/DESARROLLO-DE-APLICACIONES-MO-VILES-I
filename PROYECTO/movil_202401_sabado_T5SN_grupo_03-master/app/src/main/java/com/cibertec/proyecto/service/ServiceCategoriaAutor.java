package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Grado;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceCategoriaAutor {
    @GET("servicio/util/listaGrado")
    public Call<List<Grado>> listaTodos();
}
