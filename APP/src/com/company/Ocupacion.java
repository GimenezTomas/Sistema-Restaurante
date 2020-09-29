package com.company;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Ocupacion {
    private int nMesa;
    private String fecha;
    private int nOcupacion;
    public static int count=1;

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

    public int getnMesa() {
        return nMesa;
    }

    public void setnMesa(int nMesa) {
        this.nMesa = nMesa;
    }

    public Ocupacion(int nMesa) {
        this.nMesa = nMesa;
        this.fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.nOcupacion = count++;
    }

    public Ocupacion(int nMesa, String fecha, int nOcupacion){
        this.nMesa = nMesa;
        this.nOcupacion = nOcupacion;
        this.fecha = fecha;
    }
}
