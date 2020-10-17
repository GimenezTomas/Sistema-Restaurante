package com.company;

import javax.swing.*;
import java.io.File;
import java.util.HashSet;

public class Mesa {
    private int numMesa;
    private boolean Ocupada;
    private File QR;
    public static int count = 1;
    
    public int getNumMesa() {
        return numMesa;
    }
    public void setNumMesa(int numMesa) {
        this.numMesa = numMesa;
    }
    public boolean isOcupada() {
        return Ocupada;
    }
    public void setOcupada(boolean ocupada) {
        Ocupada = ocupada;
    }
    public File getQR() {
        return QR;
    }
    public void setQR(File QR) {
        this.QR = QR;
    }

    public Mesa(int id, File qr, boolean ocupada){
        this.numMesa = id;
        this.Ocupada = ocupada;
        this.QR = qr;
        count = id+1;
    }

    public Mesa() {
        this.numMesa = count++;
        this.Ocupada = false;
    }

    public static boolean comprobarMesa(HashSet<Mesa> mesas, int textfield){
        for (Mesa mesa : mesas) {
            if (textfield == mesa.numMesa) {
                return true;
            }
        }
        return false;
    }

    public static void agregarMesas(HashSet<Mesa> mesas,int textfield) {
        for (int i = 1; i <= textfield; i++) {
            Mesa mesa = new Mesa();
            mesas.add(mesa);
        }
    }

    public static void ocuparMesas(HashSet<Mesa>mesas, int textfield) {
        for (Mesa mesa : mesas) {
            if (textfield == mesa.numMesa && mesa.Ocupada == false) {
                mesa.Ocupada = true;
                JOptionPane.showMessageDialog(null, "La mesa se ocupo correctamente");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "La mesa esta ocupada");
    }

    public static void desocuparMesas(HashSet<Mesa>mesas, int textfield){
        for (Mesa mesa : mesas) {
            if (textfield == mesa.numMesa && mesa.Ocupada == true) {
                mesa.Ocupada = false;
                JOptionPane.showMessageDialog(null, "La mesa se desocupo correctamente");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "La mesa ya esta desocupada");
    }

    public static void borrarMesa(HashSet<Mesa>mesas, int textfield){
        for (Mesa mesa : mesas) {
            if (textfield == mesa.numMesa) {
                mesas.remove(mesa);
                JOptionPane.showMessageDialog(null, "La mesa se borro correctamente");
                return;
            }
        }
    }
}


