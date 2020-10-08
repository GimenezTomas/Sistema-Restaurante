package com.company;

import java.util.ArrayList;
import java.util.HashMap;

public class PlatoPedido extends PlatoAbs{
    private boolean entregado = false;
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

    public PlatoPedido(String nombre, Float precio, HashMap<String, Float> agregados) {
        super(nombre, precio);
        this.agregados = agregados;
    }
}
