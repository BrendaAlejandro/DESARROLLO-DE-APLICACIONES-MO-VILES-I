package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Categoria;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceCategoria {
    @GET("servicio/util/listaCategoriaDeEditorial")
    public Call<List<Categoria>> listaCategoria();
}
