package com.example.demo;

import java.io.File;
import java.util.HashSet;

public class Plato extends PlatoAbs{
    private File img;
    private String descripcion;
    private String tiempoDemora;
    private HashSet<TipoAgregados> agregados = new HashSet<>();

    //GETTERS && SETTERS
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

    //CONSTRUCTOR

    public Plato(String nombre, float precio, File img, String descripcion, String tiempoDemora) {
        super(nombre, precio);
        this.img = img;
        this.descripcion = descripcion;
        this.tiempoDemora = tiempoDemora;
    }

    public Plato(String nombre, float precio, File img, String descripcion, String tiempoDemora, HashSet<TipoAgregados> agregados) {
        super(nombre, precio);
        this.img = img;
        this.descripcion = descripcion;
        this.tiempoDemora = tiempoDemora;
        this.agregados = agregados;
    }
}
