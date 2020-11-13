package com.company;

import java.io.File;
import java.util.HashSet;

public class Plato extends PlatoAbs{
    private File imagen;
    private String descripcion;
    private String demora;
    private HashSet<TipoAgregados> agregados = new HashSet<>();

    //GETTERS && SETTERS
    public File getImagen() {
        return imagen;
    }

    public void setImagen(File img) {
        this.imagen = img;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDemora() {
        return demora;
    }

    public void setDemora(String tiempoDemora) {
        this.demora = tiempoDemora;
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
        this.imagen = img;
        this.descripcion = descripcion;
        this.demora = tiempoDemora;
    }

    public Plato(String nombre, float precio, File img, String descripcion, String tiempoDemora, HashSet<TipoAgregados> agregados) {
        super(nombre, precio);
        this.imagen = img;
        this.descripcion = descripcion;
        this.demora = tiempoDemora;
        this.agregados = agregados;
    }
}
