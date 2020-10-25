package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SeccionesPlatos {
    private String nombre;
    private ArrayList<Plato> platos = new ArrayList<>();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Plato> getPlatos() {
        return platos;
    }

    public void setPlatos(ArrayList<Plato> platos) {
        this.platos = platos;
    }

    public SeccionesPlatos(String nombre, ArrayList<Plato> platos) {
        this.nombre = nombre;
        this.platos = platos;
    }

    public SeccionesPlatos(String nombre) {
        this.nombre = nombre;
    }
}
