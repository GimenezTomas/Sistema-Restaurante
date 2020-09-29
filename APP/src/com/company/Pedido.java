package com.company;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Pedido {
    private HashMap<String, Integer> platos = new HashMap<>();
    public static int count=1;
    private int nPedido;
    private boolean entregado=false;
    private String fecha;
    private int nOcupacion;
    //GETTERS && SETTERS

    public int getnOcupacion() {
        return nOcupacion;
    }

    public void setnOcupacion(int nOcupacion) {
        this.nOcupacion = nOcupacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isEntregado() {
        return entregado;
    }

    public void setEntregado(boolean entregado) {
        this.entregado = entregado;
    }

    public HashMap<String, Integer> getPlatos() {
        return platos;
    }

    public void setPlatos(HashMap<String, Integer> platos) {
        this.platos = platos;
    }

    public int getnPedido() {
        return nPedido;
    }

    public void setnPedido(int nPedido) {
        this.nPedido = nPedido;
    }
    //CONSTRUCTOR
    public Pedido(HashMap<String, Integer> platos, int nOcupacion) {
        this.platos = platos;
        this.nPedido = count++;
        this.fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.nOcupacion=nOcupacion;
    }
    public Pedido(HashMap<String, Integer> platos, int nOcupacion, int nPedido, String fecha, boolean entregado){
        this.platos = platos;
        this.nPedido = nPedido;
        this.fecha = fecha;
        this.nOcupacion=nOcupacion;
        this.entregado = entregado;
        count = nPedido + 1;
    }
}
