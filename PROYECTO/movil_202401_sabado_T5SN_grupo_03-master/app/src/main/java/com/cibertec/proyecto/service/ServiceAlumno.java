package com.cibertec.proyecto.service;

import com.cibertec.proyecto.entity.Alumno;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceAlumno {

    @GET("servicio/alumno/porNombre/{nombre}")
    public Call<List<Alumno>> listaPorNombre(@Path("nombre")String nombre);

    @POST("servicio/alumno")
    public Call<Alumno> registra(@Body Alumno objAlumno);

    @PUT("servicio/alumno")
    public Call<Alumno> actualiza(@Body Alumno objAlumno);

    @GET("servicio/alumno/porNombreApellidoIgual/{nombre}-{apellidos}")
    public Call<List<Alumno>> listaPorNombreApellidoIgual(@Path("nombre")String nombre, @Path("apellidos")String apellidos);
}
