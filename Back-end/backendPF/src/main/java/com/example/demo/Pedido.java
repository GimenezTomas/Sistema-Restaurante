package com.example.demo;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Pedido {
    public static int count=1;
    private int nPedido;
    private boolean abierto=true;
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

    public Pedido( int nMesa, ArrayList<PlatoPedido> platos, Date fecha) {
        this.nPedido = count++;
        this.fecha = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(fecha);
        this.nMesa = nMesa;
        this.platos = platos;
    }

    public Pedido( int nMesa, ArrayList<PlatoPedido> platos, String fecha, int id) {
        this.nPedido = id;
        count = id+1;
        this.fecha = fecha;
        this.nMesa = nMesa;
        this.platos = platos;
    }
    public Pedido(){

    }
}
