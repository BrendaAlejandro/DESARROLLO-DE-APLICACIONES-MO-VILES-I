package com.cibertec.proyecto.entity;

import java.io.Serializable;

public class Grado implements Serializable {

    private int idGrado;
    private String  descripcion;

    public int getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(int idGrado) {
        this.idGrado = idGrado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
