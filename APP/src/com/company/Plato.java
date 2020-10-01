package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Plato {
    private String nombre;
    private float precio;
    private File img;
    private String descripcion;
    private String tiempoDemora;
    private HashSet<TipoAgregados> agregados = new HashSet<>();
    //private HashMap<String, HashMap<Boolean, HashMap<String, Float>>> agregados = new HashMap<>();
    //private HashMap<String/*tipo ej:salsas*/, HashMap<String/*importancia, ej:tipo:"porcion" importancia:"true=indispensable/false = despreciable"*/, HashMap<String, Float>/*agregado, ej: key="cheddar" value=15p*/>> agregados;

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

    public File getImg() {
        return img;
    }

    public void setImg(File img) {
        this.img = img;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTiempoDemora() {
        return tiempoDemora;
    }

    public void setTiempoDemora(String tiempoDemora) {
        this.tiempoDemora = tiempoDemora;
    }

    public HashSet<TipoAgregados> getAgregados() {
        return agregados;
    }

    public void setAgregados(HashSet<TipoAgregados> agregados) {
        this.agregados = agregados;
    }
/*public HashMap<String, HashMap<Boolean, HashMap<String, Float>>> getAgregados() {
        return agregados;
    }

    public void setAgregados(HashMap<String, HashMap<Boolean, HashMap<String, Float>>> agregados) {
        this.agregados = agregados;
    }*/

    //CONSTRUCTOR

    public Plato(String nombre, float precio, File img, String descripcion, String tiempoDemora) {
        this.nombre = nombre;
        this.precio = precio;
        this.img = img;
        this.descripcion = descripcion;
        this.tiempoDemora = tiempoDemora;
    }

    public Plato(String nombre, float precio, File img, String descripcion, String tiempoDemora, HashSet<TipoAgregados> agregados) {
        this.nombre = nombre;
        this.precio = precio;
        this.img = img;
        this.descripcion = descripcion;
        this.tiempoDemora = tiempoDemora;
        this.agregados = agregados;
    }
/*public Plato(String nombre, float precio, File img, String descripcion, String tiempoDemora, HashMap<String, HashMap<Boolean, HashMap<String, Float>>> agregados) {
        this.nombre = nombre;
        this.precio = precio;
        this.img = img;
        this.descripcion = descripcion;
        this.tiempoDemora = tiempoDemora;
        this.agregados = agregados;
    }*/
}
