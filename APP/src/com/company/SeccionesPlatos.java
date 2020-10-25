package com.company;

import java.util.HashMap;
import java.util.HashSet;

public class SeccionesPlatos {
    private String nombre;
    private HashSet<Plato> platos = new HashSet<>();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public HashSet<Plato> getPlatos() {
        return platos;
    }

    public void setPlatos(HashSet<Plato> platos) {
        this.platos = platos;
    }

    public SeccionesPlatos(String nombre, HashSet<Plato> platos) {
        this.nombre = nombre;
        this.platos = platos;
    }

    public SeccionesPlatos(String nombre) {
        this.nombre = nombre;
    }
}
