package com.cibertec.proyecto.entity;

import java.io.Serializable;

public class TipoProveedor implements Serializable {
    private int idTipoProveedor;
    private String descripcion;

    public int getIdTipoProveedor() {
        return idTipoProveedor;
    }

    public void setIdTipoProveedor(int idTipoProveedor) {
        this.idTipoProveedor = idTipoProveedor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
