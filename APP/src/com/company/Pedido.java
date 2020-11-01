package com.company;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Pedido {
    public static int count=1;
    private final int nPedido;
    private boolean abierto=true;
    private final String fecha;
    private final int nMesa;
    private ArrayList<PlatoPedido> platos = new ArrayList<>();

    //GETTERS && SETTERS

    public int getnPedido() {
        return nPedido;
    }

    public boolean isAbierto() {
        return abierto;
    }

    public void setAbierto(boolean abierto) {
        this.abierto = abierto;
    }

    public String getFecha() {
        return fecha;
    }

    public int getnMesa() {
        return nMesa;
    }

    public ArrayList<PlatoPedido> getPlatos() {
        return platos;
    }

    public void setPlatos(ArrayList<PlatoPedido> platos) {
        this.platos = platos;
    }

    //CONSTRUCTOR

    public Pedido( int nMesa, ArrayList<PlatoPedido> platos, String fecha, int id) {
        this.nPedido = id;
        count = id+1;
        this.fecha = fecha;
        this.nMesa = nMesa;
        this.platos = platos;
    }
}
