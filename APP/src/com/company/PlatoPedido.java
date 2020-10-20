package com.company;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class PlatoPedido extends PlatoAbs implements Comparable<PlatoPedido>{
    private boolean entregado = false;
    private Date fecha;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public PlatoPedido(String nombre, Float precio, HashMap<String, Float> agregados, Date fecha, boolean entregado) {
        super(nombre, precio);
        this.agregados = agregados;
        this.fecha = fecha;
        //this.id = count++;
        this.entregado = entregado;
    }

    @Override
    public int compareTo(PlatoPedido o) {
        if (o.getFecha().after(fecha)){
            return -1;
        }else if (o.getFecha().after(fecha)){
            return 0;
        }else{
            return 1;
        }
    }
}
