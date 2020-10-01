package com.company;

import java.util.HashMap;

public class TipoAgregados {
    private String nombre;
    private Boolean indispensable;
    private HashMap<String, Float> agregados = new HashMap<>();/*<Nombre, Precio>*/

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getIndispensable() {
        return indispensable;
    }

    public void setIndispensable(Boolean indispensable) {
        this.indispensable = indispensable;
    }

    public HashMap<String, Float> getAgregados() {
        return agregados;
    }

    public void setAgregados(HashMap<String, Float> agregados) {
        this.agregados = agregados;
    }

    public TipoAgregados(String nombre, Boolean indispensable) {
        this.nombre = nombre;
        this.indispensable = indispensable;
    }

    public TipoAgregados(String nombre, Boolean indispensable, HashMap<String, Float> agregados) {
        this.nombre = nombre;
        this.indispensable = indispensable;
        this.agregados = agregados;
    }
}
