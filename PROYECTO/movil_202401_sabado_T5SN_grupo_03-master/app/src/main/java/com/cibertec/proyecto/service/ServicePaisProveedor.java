package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Pais;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServicePaisProveedor {
    @GET("servicio/util/listaPais")
    public Call<List<Pais>> listaTodos();
}
