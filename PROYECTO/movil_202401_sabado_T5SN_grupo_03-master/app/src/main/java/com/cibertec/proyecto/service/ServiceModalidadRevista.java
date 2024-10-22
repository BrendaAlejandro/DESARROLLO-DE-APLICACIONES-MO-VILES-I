package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Modalidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceModalidadRevista {
    @GET("servicio/util/listaModalidad")
    public Call<List<Modalidad>> listaTodos();
}
