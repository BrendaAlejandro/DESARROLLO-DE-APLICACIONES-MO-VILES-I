package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Libro;
import com.cibertec.proyecto.entity.Proveedor;
import com.cibertec.proyecto.entity.Revista;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceProveedor {
    @GET("servicio/proveedor/porRazonSocial/{razSoc}")
    public Call<List<Proveedor>> listaPorRazonSocial(@Path("razSoc")String razSoc);

    @POST("servicio/proveedor")
    public Call<Proveedor> registra (@Body Proveedor obProveedor);

    @GET("servicio/proveedor/porRazonSocialIgual/{razSoc}")
    public Call<List<Proveedor>> listaProveedorPorRazonSocialIgual(@Path("razSoc") String razSoc);

    @PUT("servicio/proveedor")
    public Call<Proveedor> actualizaProveedor(@Body Proveedor objProveedor);
}



