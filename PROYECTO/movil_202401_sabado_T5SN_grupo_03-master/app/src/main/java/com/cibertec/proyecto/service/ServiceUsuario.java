package com.cibertec.proyecto.service;
import com.cibertec.proyecto.entity.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
public interface ServiceUsuario {
    @GET("servicio/usuario/porNombre/{nombre}")
    public Call<List<Usuario>> listaPorNombre(@Path("nombre")String nombre);

}
