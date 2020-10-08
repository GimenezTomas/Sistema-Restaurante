package com.company;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Pedido {
    public static int count=1;
    private int nPedido;
    private boolean abierto=false;
    private String fecha;
    private int nMesa;
    private ArrayList<PlatoPedido> platos = new ArrayList<>();

    //GETTERS && SETTERS

    public int getnPedido() {
        return nPedido;
    }

    public void setnPedido(int nPedido) {
        this.nPedido = nPedido;
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

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getnMesa() {
        return nMesa;
    }

    public void setnMesa(int nMesa) {
        this.nMesa = nMesa;
    }

    public ArrayList<PlatoPedido> getPlatos() {
        return platos;
    }

    public void setPlatos(ArrayList<PlatoPedido> platos) {
        this.platos = platos;
    }

    //CONSTRUCTOR

    public Pedido(int nPedido, boolean abierto, String fecha, int nMesa, ArrayList<PlatoPedido> platos) {
        this.nPedido = count++;
        this.abierto = abierto;
        this.fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.nMesa = nMesa;
        this.platos = platos;
    }
}
