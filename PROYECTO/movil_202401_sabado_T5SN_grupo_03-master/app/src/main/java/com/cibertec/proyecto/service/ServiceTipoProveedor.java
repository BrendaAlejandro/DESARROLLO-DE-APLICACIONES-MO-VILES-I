package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.TipoProveedor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceTipoProveedor {
    @GET("servicio/util/listaTipoProveedor")
    public Call<List<TipoProveedor>> listaTodos();
}
