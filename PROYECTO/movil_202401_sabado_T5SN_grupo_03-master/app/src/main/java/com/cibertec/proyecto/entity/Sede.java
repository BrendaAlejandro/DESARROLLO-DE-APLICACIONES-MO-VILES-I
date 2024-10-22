package com.cibertec.proyecto.entity;

import java.io.Serializable;

public class Sede implements Serializable {

    private int idSede;
    private String nombre;

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
