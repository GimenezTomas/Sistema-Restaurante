package com.example.demo;

import java.util.Date;
import java.util.HashMap;

public class PlatoPedido extends PlatoAbs{
    private boolean entregado = false;
    private String fecha;
    private HashMap<String, Float> agregados=new HashMap<>();

    public boolean isEntregado() {
        return entregado;
    }

    public void setEntregado(boolean entregado) {
        this.entregado = entregado;
    }

    public HashMap<String, Float> getAgregados() {
        return agregados;
    }

    public void setAgregados(HashMap<String, Float> agregados) {
        this.agregados = agregados;
    }

    public PlatoPedido(String nombre, Float precio) {
        super(nombre, precio);
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public PlatoPedido(String nombre, Float precio, HashMap<String, Float> agregados, String fecha, boolean entregado) {
        super(nombre, precio);
        this.agregados = agregados;
        this.fecha = fecha;
        this.entregado = entregado;
    }
}
