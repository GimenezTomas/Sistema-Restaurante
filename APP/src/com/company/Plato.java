package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Plato {
    private String nombre;
    private float precio;
    private File img;
    private String descripcion;
    private String tiempoDemora;
    private HashMap<String, HashMap<String, HashMap<String, Float>>> agregados;
    //private HashMap<String/*tipo ej:salsas*/, HashMap<String/*importancia, ej:tipo:"porcion" importancia:"indispensable"*/, HashMap<String, Float>/*agregado, ej: key="cheddar" value=15p*/>> agregados;

    //GETTERS && SETTERS
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    //CONSTRUCTOR
    public Plato(String nombre, float precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
}
